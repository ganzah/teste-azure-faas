package br.unb.ppgi.tsc;

public class RequisicaoDeAlinhamento {
    private String seq1;
    private String seq2;
    private int gap;
    private int match;
    private int mismatch;

    public RequisicaoDeAlinhamento(String seq1, String seq2, int gap, int match, int mismatch) {
        this.seq1 = seq1;
        this.seq2 = seq2;
        this.gap = gap;
        this.match = match;
        this.mismatch = mismatch;
    }

    public String getSeq1() {
        return seq1;
    }

    public String getSeq2() {
        return seq2;
    }

    public int getGap() {
        return gap;
    }

    public int getMatch() {
        return match;
    }

    public int getMismatch() {
        return mismatch;
    }
}
