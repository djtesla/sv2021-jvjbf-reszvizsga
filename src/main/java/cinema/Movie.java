package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private long id;
    private String title;
    private LocalDateTime date;
    private int maxSpaces;
    private int freeSpaces;

    public void reserve(int reservation) {
        int freeSpacesNew = freeSpaces - reservation;
        if (freeSpacesNew >= 0) {
           setFreeSpaces(freeSpacesNew);
        } else {
            throw new IllegalStateException("Not enough free spaces for reservation");
        }
    }

}
