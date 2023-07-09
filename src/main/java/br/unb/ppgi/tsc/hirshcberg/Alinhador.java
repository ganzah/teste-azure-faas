package br.unb.ppgi.tsc.hirshcberg;

public class Alinhador {
    private Parametros params;

    public Alinhador(Parametros params) {
        this.params = params;
    }
    
    public Alinhamento alinhar(String seq1, String seq2) {
        if (seq1.length() <= 1 || seq2.length() <= 1) {
            return needlemanWunsch(seq1, seq2);
        }

        var n = seq1.length() / 2;

        var left = scoreLeft(seq1.substring(0, n), seq2);
        var right = scoreRight(seq1.substring(n), seq2);
        var max = left[0] + right[0];
        var m = 0;
        for (int i = 1; i < left.length; i++) {
            var sum = left[i] + right[i];
            if (sum > max) {
                max = sum;
                m = i;

            }
        }
        
        var alignment1 = alinhar(seq1.substring(0, n), seq2.substring(0, m));
        var alignment2 = alinhar(seq1.substring(n), seq2.substring(m));
        var alignment = combinar(alignment1, alignment2);

        return alignment;
    }

    private Alinhamento combinar(Alinhamento alinhamento1, Alinhamento alinhamento2) {
        return new Alinhamento(
            alinhamento1.score() + alinhamento2.score(), 
            alinhamento1.seq1Alinhada() + alinhamento2.seq1Alinhada(), 
            alinhamento1.seq2Alinhada() + alinhamento2.seq2Alinhada());
    }
    
    private int[] scoreLeft(String seq1, String seq2) {
        var n = seq1.length();
        var m = seq2.length();
        var matrix = new int[2][m + 1];
        matrix[0][0] = 0;
        for (int j = 1; j <= m; j++) {
            matrix[0][j] = matrix[0][j - 1] + params.gap();
        }
        for (int i = 1; i <= n; i++) {
            matrix[1][0] = matrix[0][0] + params.gap();
            for (int j = 1; j <= m; j++) {
                var a = matrix[0][j - 1] + params.matchOrMismath(seq1.charAt(i - 1), seq2.charAt(j - 1));
                var b = matrix[0][j] + params.gap();
                var c = matrix[1][j - 1] + params.gap();
                matrix[1][j] = Math.max(a, Math.max(b, c));
            }
            System.arraycopy(matrix[1], 0, matrix[0], 0, matrix[1].length);
        }
        return matrix[1];
    }

    private int[] scoreRight(String seq1, String seq2) {
        var n = seq1.length();
        var m = seq2.length();
        var matrix = new int[2][m + 1];
        matrix[1][m] = 0;
        for (int j = m - 1; j >= 0; j--) {
            matrix[1][j] = matrix[1][j + 1] + params.gap();
        }
        for (int i = n - 1; i >= 0; i--) {
            matrix[0][m] = matrix[1][m] + params.gap();
            for (int j = m - 1; j >= 0; j--) {
                var a = matrix[1][j + 1] + params.matchOrMismath(seq1.charAt(i), seq2.charAt(j));
                var b = matrix[1][j] + params.gap();
                var c = matrix[0][j + 1] + params.gap();
                matrix[0][j] = Math.max(a, Math.max(b, c));
            }
            System.arraycopy(matrix[0], 0, matrix[1], 0, matrix[0].length);
        }
        return matrix[1];
    }

    private Alinhamento needlemanWunsch(String seq1, String seq2) {
        var n = seq1.length();
        var m = seq2.length();

        var matriz = new int[n + 1][m + 1];

        for (int j = 0; j <= m; j++) {
            matriz[0][j] = j * params.gap();
        }
        for (int i = 0; i <= n; i++) {
            matriz[i][0] = i * params.gap();
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                var del = matriz[i - 1][j] + params.gap();
                var ins = matriz[i][j - 1] + params.gap();
                int matchOuMismatch = matriz[i - 1][j - 1] + params.matchOrMismath(seq1.charAt(i - 1), seq2.charAt(j - 1));
                matriz[i][j] = Math.max(del, Math.max(ins, matchOuMismatch));
            }
        }

        if (params.apenasScore()) {
            return new Alinhamento(matriz[n][m], "", "");
        }
        
        var seq1Alinhada = "";
        var seq2Alinhada = "";
        var i = n;
        var j = m;
        while (i > 0 || j > 0) {

            var matchOuMismath = i > 0 && j > 0 && matriz[i][j] == matriz[i - 1][j - 1]
                    + params.matchOrMismath(seq1.charAt(i - 1), seq2.charAt(j - 1));
            var del = i > 0 && matriz[i][j] == matriz[i - 1][j] + params.gap();
            var ins = j > 0 && matriz[i][j] == matriz[i][j - 1] + params.gap();
            if (matchOuMismath) {
                seq1Alinhada = seq1.charAt(i - 1) + seq1Alinhada;
                seq2Alinhada = seq2.charAt(j - 1) + seq2Alinhada;
                i -= 1;
                j -= 1;
            } else if (del) {
                seq1Alinhada = seq1.charAt(i - 1) + seq1Alinhada;
                seq2Alinhada = "-" + seq2Alinhada;
                i -= 1;
            } else if (ins) {
                seq1Alinhada = "-" + seq1Alinhada;
                seq2Alinhada = seq2.charAt(j - 1) + seq2Alinhada;
                j -= 1;
            }
        }

        return new Alinhamento(matriz[n][m], seq1Alinhada, seq2Alinhada);
    }
}
