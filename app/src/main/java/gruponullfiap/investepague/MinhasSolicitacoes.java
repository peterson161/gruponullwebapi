package gruponullfiap.investepague;

/**
 * Created by peterson161 on 10/24/17.
 */

public class MinhasSolicitacoes {
    private int investidor_id_investidor;
    private int id_solicitacao;
    private String data_criacao;
    private double valor;
    private int meses_duracao;
    private double perc_juros_mes;

    public MinhasSolicitacoes(int investidor_id_investidor, int id_solicitacao, String data_criacao, double valor, int meses_duracao, double perc_juros_mes) {
        this.investidor_id_investidor = investidor_id_investidor;
        this.id_solicitacao = id_solicitacao;
        this.data_criacao = data_criacao;
        this.valor = valor;
        this.meses_duracao = meses_duracao;
        this.perc_juros_mes = perc_juros_mes;
    }

    @Override
    public String toString() {
        return "Data solicitação: " + data_criacao + "  Valor: R$ " + valor + "   Meses: " + meses_duracao + "    Tx.: " + perc_juros_mes + " % a.m.";
    }

    public int getInvestidor_id_investidor() {
        return investidor_id_investidor;
    }

    public void setInvestidor_id_investidor(int investidor_id_investidor) {
        this.investidor_id_investidor = investidor_id_investidor;
    }

    public int getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(int id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getMeses_duracao() {
        return meses_duracao;
    }

    public void setMeses_duracao(int meses_duracao) {
        this.meses_duracao = meses_duracao;
    }

    public double getPerc_juros_mes() {
        return perc_juros_mes;
    }

    public void setPerc_juros_mes(double perc_juros_mes) {
        this.perc_juros_mes = perc_juros_mes;
    }
}
