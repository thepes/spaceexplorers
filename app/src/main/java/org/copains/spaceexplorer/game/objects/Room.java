package org.copains.spaceexplorer.game.objects;

import org.copains.spaceexplorer.tactical.objects.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 06/07/2016.
 */

public class Room {

    private List<Coordinates> cells;
    private boolean open;

    public void addCell(Coordinates coord) {
        if (null == cells) {
            cells = new ArrayList<>();
        }
        cells.add(coord);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
