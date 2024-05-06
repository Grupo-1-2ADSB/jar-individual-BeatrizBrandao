package com.medtech.model.mensagemSlack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class ChatGeralAvisoRAMAlta {
    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";
    private static int ramUsage = 80; // Por exemplo, 80% de uso da RAM

    public static void main(String[] args) {
        enviarMensagem();
    }

    public static void enviarMensagem() {
        JSONObject generalMessage = createSlackMessage();
        String generalMessageAsString = generalMessage.toString();
        sendMessageToSlackWebhook(SLACK_WEBHOOK_URL, generalMessageAsString);
    }

    public static void sendMessageToSlackWebhook(String webhookUrl, String message) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String jsonPayload = message;

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully to Slack via webhook!");
            } else {
                System.out.println("Failed to send message to Slack via webhook. Response code: " + responseCode);
            }
        } catch (Exception ex) {
            System.err.println("Error sending message to Slack via webhook: " + ex.getMessage());
        }
    }
    public static JSONObject createSlackMessage() {
        JSONObject blocks = new JSONObject()
                .put("blocks", new JSONArray()
                        .put(new JSONObject()
                                .put("type", "rich_text")
                                .put("elements", new JSONArray()
                                        .put(new JSONObject()
                                                .put("type", "rich_text_section")
                                                .put("elements", new JSONArray()
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", "🚨 Equipe do NOC, \n \n")
                                                                .put("style", new JSONObject().put("bold", true))
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("type", "rich_text")
                                .put("elements", new JSONArray()
                                        .put(new JSONObject()
                                                .put("type", "rich_text_section")
                                                .put("elements", new JSONArray()
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", "Por favor, precisamos de um técnico para investigar um problema persistente de ")
                                                        )
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", "alta urgência ")
                                                                .put("style", new JSONObject().put("bold", true))
                                                        )
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", " relacionado à memória RAM em um dos computadores da recepção.")
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject().put("type", "divider"))
                        .put(new JSONObject()
                                .put("type", "rich_text")
                                .put("elements", new JSONArray()
                                        .put(new JSONObject()
                                                .put("type", "rich_text_section")
                                                .put("elements", new JSONArray()
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", "Detalhes do caso:\n")
                                                        )
                                                )
                                        )
                                        .put(new JSONObject()
                                                .put("type", "rich_text_list")
                                                .put("style", "bullet")
                                                .put("elements", new JSONArray()
                                                        .put(new JSONObject()
                                                                .put("type", "rich_text_section")
                                                                .put("elements", new JSONArray()
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Número de Identificação: ")
                                                                        )
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "001")
                                                                        )
                                                                )
                                                        )
                                                        .put(new JSONObject()
                                                                .put("type", "rich_text_section")
                                                                .put("elements", new JSONArray()
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Nome do Computador: ")
                                                                        )
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Nominho do PC")
                                                                        )
                                                                )
                                                        )
                                                        .put(new JSONObject()
                                                                .put("type", "rich_text_section")
                                                                .put("elements", new JSONArray()
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Localização: ")
                                                                        )
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Recepção")
                                                                        )
                                                                )
                                                        )
                                                        .put(new JSONObject()
                                                                .put("type", "rich_text_section")
                                                                .put("elements", new JSONArray()
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "Endereço IP: ")
                                                                        )
                                                                        .put(new JSONObject()
                                                                                .put("type", "text")
                                                                                .put("text", "XX.XXX.XXX")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject().put("type", "divider"))
                        .put(new JSONObject()
                                .put("type", "section")
                                .put("text", new JSONObject()
                                        .put("type", "mrkdwn")
                                        .put("text", "Técnico Designado: ")
                                )
                                .put("accessory", new JSONObject()
                                        .put("type", "users_select")
                                        .put("placeholder", new JSONObject()
                                                .put("type", "plain_text")
                                                .put("text", "Selecione um usuário")
                                                .put("emoji", true)
                                        )
                                        .put("action_id", "users_select_action")
                                )
                        )
                        .put(new JSONObject()
                                .put("type", "rich_text")
                                .put("elements", new JSONArray()
                                        .put(new JSONObject()
                                                .put("type", "rich_text_section")
                                                .put("elements", new JSONArray()
                                                        .put(new JSONObject()
                                                                .put("type", "text")
                                                                .put("text", "Atenciosamente, \nEquipe MedTech.")
                                                        )
                                                )
                                        )
                                )
                        )
                );

        return blocks;
    }
}
