package GeneticalAlgorithm;

class Chromosome {
    private double veight;
    private double bigramFrequency;
    private double letterFrequency;

    double getLetterFrequency() {
        return letterFrequency;
    }

    void setLetterFrequency(double letterFrequency) {
        this.letterFrequency = letterFrequency;
    }

    void setVeight(double veight) {
        this.veight = veight;
    }

    double getVeight() {
        return veight;
    }

    void setBigramFrequency(double bigramFrequency) {
        this.bigramFrequency = bigramFrequency;
    }

    double getBigramFrequency() {
        return bigramFrequency;
    }
}

