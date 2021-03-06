package com.demobank.gemfire.functions;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class RealtimeStockClient {
    public Map getStockPrices(String symbol) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        Request request = new Request.Builder()
                .url("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&outputsize=full&apikey=EW8WXQRQTMML87P")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseJson = response.body().string();
            Map map = JsonSerDes.deserialize(responseJson);
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
