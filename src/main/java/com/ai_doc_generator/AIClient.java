package com.ai_doc_generator;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;

public class AIClient {

	private final OpenAIClient client;

	public AIClient() {
		// Create the OpenAI client instance
		this.client = OpenAIOkHttpClient.fromEnv();
	}

	public String generateDocumentation(String procName, String procBody) {
		String prompt = String.format("""
				You are an expert in legacy mainframe code analysis and documentation.

				Given the following procedure, generate concise, professional documentation that is suitable for a business or technical design document.

				Your response must follow this format exactly:

				Procedure: <<%s>>
				Description: (Briefly summarize what this procedure does in plain English — 1-2i sentences, no technical noise.)
				Business Rules: (Summarize the key logic or decision rules as clear bullet points, not line-by-line code translation.)

				Keep it readable and structured like this example:

				Procedure: <<EXAMPLE>>
				Description: Handles student term validation and determines whether to read current or historical enrollment data.
				Business Rules:
				- Loads input parameters.
				- Checks for term mismatch and retrieves correct term info.
				- Routes to the correct data read procedure (ECS or EHS).
				- Writes output parameters at the end.

				Now generate documentation for this procedure:
				%s
				""", procName, procBody);

		ChatCompletionUserMessageParam userMessage =
				ChatCompletionUserMessageParam.builder()
				.content(prompt)
				.build();

		// Create chat completion parameters
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
				.model("gpt-5-nano")
				.addMessage(ChatCompletionMessageParam.ofUser(userMessage))
				.build();

		// the create() call now returns a ChatCompletion record
        ChatCompletion completion = client.chat().completions().create(params);

        // choices is now a public list field, not accessed through a getter
        if (completion.choices() != null && !completion.choices().isEmpty()) {
            var message = completion.choices().get(0).message();
            if (message != null && message.content() != null) {
                return message.content().get();
            }
        }

        return "No content returned by model.";
	}
}


