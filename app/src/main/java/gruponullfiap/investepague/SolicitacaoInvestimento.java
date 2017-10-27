package gruponullfiap.investepague;

import java.sql.Date;

/**
 * Created by peterson161 on 10/23/17.
 */

public class SolicitacaoInvestimento {
    private int id_solicitacao;
    private Date data_criacao;
    private Date data_limite;
    private Date data_cancelamento;
    private int conf_pac_tit_id_pacote_titulo;
    private int investidor_id_investidor;
    private String status_sistema_id_status;
    private ConfigPacoteTitulo configPacoteTitulo;

    @Override
    public String toString() {
        return "Data: " + data_criacao + " Valor título: " + configPacoteTitulo.getValorTitulo() + " Meses: " + configPacoteTitulo.getMesesDuracao();
    }

    public int getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(int id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Date getData_limite() {
        return data_limite;
    }

    public void setData_limite(Date data_limite) {
        this.data_limite = data_limite;
    }

    public Date getData_cancelamento() {
        return data_cancelamento;
    }

    public void setData_cancelamento(Date data_cancelamento) {
        this.data_cancelamento = data_cancelamento;
    }

    public int getConf_pac_tit_id_pacote_titulo() {
        return conf_pac_tit_id_pacote_titulo;
    }

    public void setConf_pac_tit_id_pacote_titulo(int conf_pac_tit_id_pacote_titulo) {
        this.conf_pac_tit_id_pacote_titulo = conf_pac_tit_id_pacote_titulo;
    }

    public int getInvestidor_id_investidor() {
        return investidor_id_investidor;
    }

    public void setInvestidor_id_investidor(int investidor_id_investidor) {
        this.investidor_id_investidor = investidor_id_investidor;
    }

    public String getStatus_sistema_id_status() {
        return status_sistema_id_status;
    }

    public void setStatus_sistema_id_status(String status_sistema_id_status) {
        this.status_sistema_id_status = status_sistema_id_status;
    }

    public ConfigPacoteTitulo getConfigPacoteTitulo() {
        return configPacoteTitulo;
    }

    public void setConfigPacoteTitulo(ConfigPacoteTitulo configPacoteTitulo) {
        this.configPacoteTitulo = configPacoteTitulo;
    }
}
