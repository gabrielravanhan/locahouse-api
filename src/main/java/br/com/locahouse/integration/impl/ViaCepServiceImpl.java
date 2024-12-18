package br.com.locahouse.integration.impl;

import br.com.locahouse.exception.BusinessException;
import br.com.locahouse.mapper.CepMapper;
import br.com.locahouse.model.Cep;
import br.com.locahouse.dto.cep.CepConsultaViaCepDto;
import br.com.locahouse.integration.ViaCepService;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ViaCepServiceImpl implements ViaCepService {

    private final Gson gson;

    @Autowired
    public ViaCepServiceImpl(Gson gson) {
        this.gson = gson;
    }

    public Cep consultar(String numeroCep) throws IOException {
        HttpGet request = new HttpGet("https://viacep.com.br/ws/" + this.removerHifenCep(numeroCep) + "/json");
        CepConsultaViaCepDto cepConsultaViaCepDto = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build()) {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value())
                throw new BusinessException("CEP inválido.", HttpStatus.BAD_REQUEST);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                cepConsultaViaCepDto = this.gson.fromJson(EntityUtils.toString(httpEntity), CepConsultaViaCepDto.class);
            }
        }
        if (cepConsultaViaCepDto != null && cepConsultaViaCepDto.erro() != null && cepConsultaViaCepDto.erro().equals("true")) {
            throw new BusinessException("CEP não encontrado na base de dados do ViaCEP.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (cepConsultaViaCepDto == null) {
            throw new BusinessException("Erro ao consultar o ViaCEP.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return CepMapper.cepConsultaViaCepDtoToEntity(cepConsultaViaCepDto);
    }

    private String removerHifenCep(String numeroCep) {
        return numeroCep.replace("-", "");
    }
}
