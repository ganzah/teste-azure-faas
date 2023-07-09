package br.unb.ppgi.tsc.hirshcberg;

public record Parametros(
        int gap,
        int match,
        int mismatch,
        boolean apenasScore) {

    public int matchOrMismath(char c1, char c2) {
        return c1 == c2 ? match : mismatch;
    }
}
