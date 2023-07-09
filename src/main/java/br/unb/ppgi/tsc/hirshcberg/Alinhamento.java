package br.unb.ppgi.tsc.hirshcberg;

public record Alinhamento(
        int score,
        String seq1Alinhada,
        String seq2Alinhada) {

    public String toString() {
        return """
                Score: %d
                Seq1: %s
                      %s
                Seq2: %s
                """.formatted(score, seq1Alinhada, "|".repeat(seq1Alinhada.length()), seq2Alinhada);
    }
}
