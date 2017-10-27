package gruponullfiap.investepague;

/**
 * Created by peterson161 on 10/24/17.
 */

public class EmprestimosDisponiveis {
    private int id_solicitacao;
    private double valor;
    private int meses_duracao;
    private double perc_juros_mes;
    private int investidor_id_investidor;
    private String parcelas;

    public EmprestimosDisponiveis(int id_solicitacao, double valor, int meses_duracao, double perc_juros_mes, int investidor_id_investidor, String parcelas) {
        this.id_solicitacao = id_solicitacao;
        this.valor = valor;
        this.meses_duracao = meses_duracao;
        this.perc_juros_mes = perc_juros_mes;
        this.investidor_id_investidor = investidor_id_investidor;
        this.parcelas = parcelas;
    }

    public int getId_solicitacao() {
        return id_solicitacao;
    }

    public void setId_solicitacao(int id_solicitacao) {
        this.id_solicitacao = id_solicitacao;
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

    public int getInvestidor_id_investidor() {
        return investidor_id_investidor;
    }

    public void setInvestidor_id_investidor(int investidor_id_investidor) {
        this.investidor_id_investidor = investidor_id_investidor;
    }

    public String getParcelas() {
        return parcelas;
    }

    public void setParcelas(String parcelas) {
        this.parcelas = parcelas;
    }
}

