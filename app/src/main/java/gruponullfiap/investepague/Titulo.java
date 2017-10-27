package gruponullfiap.investepague;

/**
 * Created by peterson161 on 10/17/17.
 */

public class Titulo {
    private int idTitulo;
    private String dataEmissao;
    private double valorTitulo;
    private String dataVencimento;
    private int mesesDuracao;
    private double percJuros;
    private int idCategoria;
    private int idTituloOriginal;
    private int idPacoteTitulo;
    private int idCliente;
    private int idInvestidor;
    private int idStatus;

    public Titulo(){

    }

    public Titulo(int idTitulo, String dataEmissao, double valorTitulo, String dataVencimento, int mesesDuracao, double percJuros, int idInvestidor) {
        this.idTitulo = idTitulo;
        this.dataEmissao = dataEmissao;
        this.valorTitulo = valorTitulo;
        this.dataVencimento = dataVencimento;
        this.mesesDuracao = mesesDuracao;
        this.percJuros = percJuros;
        this.idInvestidor = idInvestidor;
    }

    public Titulo(int idTitulo, String dataEmissao, double valorTitulo, String dataVencimento, int mesesDuracao, double percJuros, int idCategoria, int idTituloOriginal, int idPacoteTitulo, int idCliente, int idInvestidor, int idStatus) {
        this.idTitulo = idTitulo;
        this.dataEmissao = dataEmissao;
        this.valorTitulo = valorTitulo;
        this.dataVencimento = dataVencimento;
        this.mesesDuracao = mesesDuracao;
        this.percJuros = percJuros;
        this.idCategoria = idCategoria;
        this.idTituloOriginal = idTituloOriginal;
        this.idPacoteTitulo = idPacoteTitulo;
        this.idCliente = idCliente;
        this.idInvestidor = idInvestidor;
        this.idStatus = idStatus;
    }

    public int getIdTitulo() {
        return idTitulo;
    }

    public void setIdTitulo(int idTitulo) {
        this.idTitulo = idTitulo;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public double getValorTitulo() {
        return valorTitulo;
    }

    public void setValorTitulo(double valorTitulo) {
        this.valorTitulo = valorTitulo;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdTituloOriginal() {
        return idTituloOriginal;
    }

    public void setIdTituloOriginal(int idTituloOriginal) {
        this.idTituloOriginal = idTituloOriginal;
    }

    public int getIdPacoteTitulo() {
        return idPacoteTitulo;
    }

    public void setIdPacoteTitulo(int idPacoteTitulo) {
        this.idPacoteTitulo = idPacoteTitulo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdInvestidor() {
        return idInvestidor;
    }

    public void setIdInvestidor(int idInvestidor) {
        this.idInvestidor = idInvestidor;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    @Override
    public String toString() {
        return "Nome: Peterson" + "Valor: 2.000,00";
    }
}
