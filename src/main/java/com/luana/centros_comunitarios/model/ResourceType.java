package com.luana.centros_comunitarios.model;

public enum ResourceType {
    MEDICO(4),
    VOLUNTARIO(3),
    KIT_MEDICO(7),
    VEICULO(5),
    CESTA_BASICA(2);

    private final int pontos;

    ResourceType(int pontos) {
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }
}
