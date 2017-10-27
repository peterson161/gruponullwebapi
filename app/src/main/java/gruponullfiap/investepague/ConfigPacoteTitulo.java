package gruponullfiap.investepague;

/**
 * Created by peterson161 on 10/17/17.
 */

public class ConfigPacoteTitulo {
    private int idPacoteTitulo;
    private String descricaoPacote;
    private double valorTitulo;
    private int mesesDuracao;
    private double percJuros;

    public ConfigPacoteTitulo(){
        //
    }

    public ConfigPacoteTitulo(int idPacoteTitulo, String descricaoPacote, double valorTitulo, int mesesDuracao, double percJuros) {
        this.idPacoteTitulo = idPacoteTitulo;
        this.descricaoPacote = descricaoPacote;
        this.valorTitulo = valorTitulo;
        this.mesesDuracao = mesesDuracao;
        this.percJuros = percJuros;
    }

    public int getIdPacoteTitulo() {
        return idPacoteTitulo;
    }

    public void setIdPacoteTitulo(int idPacoteTitulo) {
        this.idPacoteTitulo = idPacoteTitulo;
    }

    public String getDescricaoPacote() {
        return descricaoPacote;
    }

    public void setDescricaoPacote(String descricaoPacote) {
        this.descricaoPacote = descricaoPacote;
    }

    public double getValorTitulo() {
        return valorTitulo;
    }

    public void setValorTitulo(double valorTitulo) {
        this.valorTitulo = valorTitulo;
    }

    public int getMesesDuracao() {
        return mesesDuracao;
    }

    public void setMesesDuracao(int mesesDuracao) {
        this.mesesDuracao = mesesDuracao;
    }

    public double getPercJuros() {
        return percJuros;
    }

    public void setPercJuros(double percJuros) {
        this.percJuros = percJuros;
    }
}
