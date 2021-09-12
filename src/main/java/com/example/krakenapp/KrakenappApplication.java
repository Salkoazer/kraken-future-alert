package com.example.krakenapp;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
public class KrakenappApplication {
    
        private static RestTemplate template = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(KrakenappApplication.class, args);
                run();
	}
        
        private static float liveValue() {
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("api.kraken.com")
                    .path("0/public/Ticker")
                    .queryParam("pair", "ethusd")
                    .build();
            
            ResponseEntity<Live> entity = template.getForEntity(uri.toUriString(), Live.class);
            
            return Float.parseFloat(entity.getBody().getResult().getPair().getB()[0]);
        }
        
        private static float futureValue() {
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("futures.kraken.com")
                    .path("derivatives/api/v3/tickers")
                    .build();
            
            ResponseEntity<Future> entity = template.getForEntity(uri.toUriString(), Future.class);
            
            float bid = 0;
            
            Ticker[] tickers = entity.getBody().getTickers();
            
            for (int i = 0; i < tickers.length; i++) {
                if (tickers[i].getPair() != null && tickers[i].getTag() != null){
                    if (tickers[i].getPair().equals("ETH:USD") && tickers[i].getTag().equals("quarter")) {
                        bid = Float.parseFloat(tickers[i].getBid());
                    }
                }

            }
            
            return bid;
        }
        
        private static void run() {
            while(true) {
                
                System.out.println("Running...");
                
                float margin = (((futureValue() / liveValue()) - 1) * 100);
                
                if (margin > 4) {
                    
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    
                    String message = "Eth+Future+/+Eth+Live+-->+" + String.format("%.2f", margin) + "%";
                    
                    params.add("username", "Salkoazer");
                    params.add("password", "123qweasdZXC");
                    params.add("origem", "968846647");
                    params.add("destino", "918254518");
                    params.add("mensagem", message);
                    params.add("mensagemlonga", "0");
                
                    UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("www.lusosms.com")
                    .path("enviar_sms_get.php")
                    .queryParams(params)
                    .build();
                    
                    ResponseEntity<String> entity = template.getForEntity(uri.toUriString(), String.class);
                    
                    System.out.println(entity.getBody() + " valor - > " + margin + "%\nData: " + LocalDateTime.now().toString());
                    
            }
                try {
                    Thread.sleep(86400);
                } catch (InterruptedException ex) {
                    Logger.getLogger(KrakenappApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

}
