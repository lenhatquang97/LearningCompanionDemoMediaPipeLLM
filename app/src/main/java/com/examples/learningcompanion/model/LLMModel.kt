package com.examples.learningcompanion.model

import com.google.mediapipe.tasks.genai.llminference.LlmInference

enum class LLMModel(
    val path: String,
    val url: String,
    val fileName: String,
    val preferredBackend: LlmInference.Backend?,
    val thinking: Boolean,
    val temperature: Float,
    val topK: Int,
    val topP: Float,
) {
    NONE(path = "", url = "", fileName = "", preferredBackend = LlmInference.Backend.DEFAULT, thinking = false, temperature = 0.0f, topK = 0, topP = 0f),
    GEMMA3_1B_IT_CPU(
        path = "/data/local/tmp/Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task",
        url = "https://www.dropbox.com/scl/fi/mky83xnln8lr2ub3a38t3/Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task?rlkey=kdoaltng60a4r0gqxjt5cqhvg&st=ztaa63zk&dl=1",
        fileName = "Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task",
        preferredBackend = LlmInference.Backend.CPU,
        thinking = false,
        temperature = 1.0f,
        topK = 64,
        topP = 0.95f
    ),
    GEMMA3_1B_IT_GPU(
        path = "/data/local/tmp/Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task",
        url = "https://www.dropbox.com/scl/fi/mky83xnln8lr2ub3a38t3/Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task?rlkey=kdoaltng60a4r0gqxjt5cqhvg&st=ztaa63zk&dl=1",
        fileName = "Gemma3-1B-IT_multi-prefill-seq_q8_ekv2048.task",
        preferredBackend = LlmInference.Backend.GPU,
        thinking = false,
        temperature = 1.0f,
        topK = 64,
        topP = 0.95f
    ),
    QWEN2_0_5B_INSTRUCT(
        path = "/data/local/tmp/Qwen2.5-0.5B-Instruct_multi-prefill-seq_q8_ekv1280.task",
        url = "https://www.dropbox.com/scl/fi/pcc49uxru5rkhi1nmhgb6/Qwen2.5-0.5B-Instruct_multi-prefill-seq_q8_ekv1280.task?rlkey=jw54doktn43k0rv13le5kh06d&e=1&st=ajy89egf&dl=1",
        fileName = "Qwen2.5-0.5B-Instruct_multi-prefill-seq_q8_ekv1280.task",
        preferredBackend = LlmInference.Backend.CPU,
        thinking = false,
        temperature = 0.95f,
        topK = 40,
        topP = 1.0f
    ),
}