package inc.alc.beercounter.dtos;

import java.time.LocalDateTime;

public class CountDto {
    private String name;
    private LocalDateTime zeitstempel;
    private Double menge;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getZeitstempel() {
        return zeitstempel;
    }

    public void setZeitstempel(LocalDateTime zeitstempel) {
        this.zeitstempel = zeitstempel;
    }

    public Double getMenge() {
        return menge;
    }

    public void setMenge(Double menge) {
        this.menge = menge;
    }
}
