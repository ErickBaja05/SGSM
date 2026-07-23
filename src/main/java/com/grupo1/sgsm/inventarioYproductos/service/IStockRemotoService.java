package com.grupo1.sgsm.inventarioYproductos.service;

import java.util.List;
import com.grupo1.sgsm.inventarioYproductos.dto.ConsultaStockRemotoDTO;

public interface IStockRemotoService {
    List<ConsultaStockRemotoDTO> consultarStockRemoto();
}
