package com.examples.learningcompanion.singleton

import android.content.Context
import android.util.Log
import com.google.common.util.concurrent.ListenableFuture
import com.examples.learningcompanion.model.LLMModel
import com.examples.learningcompanion.viewstate.ChatUiState
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession.LlmInferenceSessionOptions
import com.google.mediapipe.tasks.genai.llminference.ProgressListener
import java.io.File
import kotlin.math.max

class InferenceSingleton private constructor(context: Context) {
    private lateinit var llmInference: LlmInference
    private lateinit var llmInferenceSession: LlmInferenceSession
    private val tag = InferenceSingleton::class.qualifiedName

    val uiState = ChatUiState(llmModel.thinking)

    init {
        if (!modelExists(context)) {
            throw IllegalArgumentException("Model not found at path: ${llmModel.path}")
        }

        createEngine(context)
        createSession()
    }

    fun close() {
        llmInferenceSession.close()
        llmInference.close()
    }

    fun resetSession() {
        llmInferenceSession.close()
        createSession()
    }

    private fun createEngine(context: Context) {
        val inferenceOptions = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelPath(context))
            .setMaxTokens(MAX_TOKENS)
            .apply { llmModel.preferredBackend?.let { setPreferredBackend(it) } }
            .build()

        try {
            llmInference = LlmInference.createFromOptions(context, inferenceOptions)
        } catch (e: Exception) {
            Log.e(tag, "Load model error: ${e.message}", e)
            throw e
        }
    }

    private fun createSession() {
        val sessionOptions =  LlmInferenceSessionOptions.builder()
            .setTemperature(llmModel.temperature)
            .setTopK(llmModel.topK)
            .setTopP(llmModel.topP)
            .build()

        try {
            llmInferenceSession =
                LlmInferenceSession.createFromOptions(llmInference, sessionOptions)
        } catch (e: Exception) {
            Log.e(tag, "LlmInferenceSession create error: ${e.message}", e)
            throw e
        }
    }

    fun generateResponseAsync(prompt: String, progressListener: ProgressListener<String>) : ListenableFuture<String> {
        llmInferenceSession.addQueryChunk(prompt)
        return llmInferenceSession.generateResponseAsync(progressListener)
    }

    fun estimateTokensRemaining(prompt: String): Int {
        val context = uiState.messages.joinToString { it.rawMessage } + prompt
        if (context.isEmpty()) return -1 // Special marker if no content has been added

        val sizeOfAllMessages = llmInferenceSession.sizeInTokens(context)
        val approximateControlTokens = uiState.messages.size * 3
        val remainingTokens = MAX_TOKENS - sizeOfAllMessages - approximateControlTokens -  DECODE_TOKEN_OFFSET
        // Token size is approximate so, let's not return anything below 0
        return max(0, remainingTokens)
    }

    companion object {
        var llmModel: LLMModel = LLMModel.GEMMA3_1B_IT_GPU
        private var instance: InferenceSingleton? = null

        fun getInstance(context: Context): InferenceSingleton {
            return if (instance != null) {
                instance!!
            } else {
                InferenceSingleton(context).also { instance = it }
            }
        }

        fun resetInstance(context: Context): InferenceSingleton {
            return InferenceSingleton(context).also { instance = it }
        }

        fun modelPathFromUrl(context: Context): String {
            if (llmModel.url.isNotEmpty()) {
                val urlFileName = llmModel.fileName
                if (urlFileName.isNotEmpty()) {
                    return File(context.filesDir, urlFileName).absolutePath
                }
            }

            return ""
        }

        fun modelPath(context: Context): String {
            val modelFile = File(llmModel.path)
            if (modelFile.exists()) {
                return llmModel.path
            }

            return modelPathFromUrl(context)
        }

        fun modelExists(context: Context): Boolean {
            return File(modelPath(context)).exists()
        }

        /** The maximum number of tokens the model can process. */
        var MAX_TOKENS = 2048

        /**
         * An offset in tokens that we use to ensure that the model always has the ability to respond when
         * we compute the remaining context length.
         */
        var DECODE_TOKEN_OFFSET = 256
    }
}
