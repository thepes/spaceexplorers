package org.copains.spaceexplorer.tactical.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

public class StarshipMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static StarshipMap instance;
	
	public static final String[] SUPPORTED_VERSIONS = {"0.0.1", "0.0.2" };

	public static final short WALL = 0;
	public static final short FLOOR = 1;
	public static final short DOOR = 2;
	public static final short START = 9;
	
    private String name;
    private int sizeX;
    private int sizeY;
    private Coordinates humanTopLeft;
    private Coordinates humanBottomRight;
    

    private short[][] reliefs;
    private Hashtable<String, List<Coordinates>> rooms;

    private StarshipMap(File map) {
        try {
            name = map.getName();
            FileReader reader = new FileReader(map);
            BufferedReader br = new BufferedReader(reader);
            String currentLine = null;
            ArrayList< String > fileContent = new ArrayList < String >();
            while ((currentLine = br.readLine()) != null) {
                fileContent.add(currentLine);
            }
            initFromStringArray(fileContent);
            } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StarshipMap(InputStream is) {
        try {
            //name = map.getName();
        	InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String currentLine;
            ArrayList< String > fileContent = new ArrayList <>();
            while ((currentLine = br.readLine()) != null) {
                fileContent.add(currentLine);
            }
            initFromStringArray(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static StarshipMap getInstance(InputStream is) {
    	instance = new StarshipMap(is);
    	return (instance);
    }
    
    public static StarshipMap getInstance() {
    	return (instance);
    }

    public short getRelief(int x, int  y) {
    	if ((x < 0) || (y < 0) || (x >= sizeX) || ( y >= sizeY)) {
    		return (-1);
    	}
    	return (reliefs[x][y]);
    }
    
    private void initReliefs(List < String > fileContent) {
    	 int startX = 0, startY = 0;
        for (int y = 0; y < sizeY; y++) {
            String line = fileContent.get(y + 1);
            for (int x = 0; x < sizeX; x++) {
                reliefs[x][y] =
                        Short.parseShort(
                                "" + line.charAt(x));
                if (reliefs[x][y] == START) {
                	if ((startX == 0) && (startY == 0)) {
                		humanTopLeft = new Coordinates(x, y);
                		startX = x;
                		startY = y;
                	}
                }
            }
        }
        humanBottomRight = new Coordinates(startX, startY);
    }

    /***
     * creates a room list from the MapFile 
     * @param fileContent
     */
    private void initRooms(List < String > fileContent) {
    	rooms = new Hashtable<String, List<Coordinates>>();
    	for (int y = sizeY ; y < sizeY*2; y++) {
    		String line = fileContent.get(y + 1);
    		for (int x = 0; x < sizeX; x++) {
    			String cell = ""+line.charAt(x);
    			try {
    				Short.parseShort(cell);
    			} catch (NumberFormatException e) {
    				// if it's not a number it's a room
    				List<Coordinates> roomCells = rooms.get(cell);
    				if (null == roomCells) {
    					roomCells = new ArrayList<>();
    					rooms.put(cell, roomCells);
    				}
    				Coordinates coord = new Coordinates(x, y-sizeY);
    				roomCells.add(coord);
    			}
    		}
    	}
    }

    private void initSize(List < String > content) {
        StringTokenizer st = new StringTokenizer(content.get(0), " ");
        String x = st.nextToken();
        String y = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(x, "=");
        st2.nextToken();
        sizeX = Integer.parseInt(st2.nextToken());
        st2 = new StringTokenizer(y, "=");
        st2.nextToken();
        sizeY = Integer.parseInt(st2.nextToken());
        reliefs = new short[sizeX][sizeY];
    }

    public String[][] getDisplayAreaPictures(int topX, int topY, int sizeX,
            int sizeY) {
        String[][] ret = new String[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                StringBuilder sb = new StringBuilder();
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if ((topX + x + j < 0) || (topY + y + i < 0)
                                || (topX + x + j > this.sizeX)
                                || (topY + y + i > this.sizeY))
                            sb.append(reliefs[topX + x][topY + y]);
                        else
                            sb.append(reliefs[topX + x + j][topY + y + i]);
                    }
                }
                sb.append(".png");
                ret[x][y] = sb.toString();
            }
        }
        return (ret);
    }

    public String getVersion(String line) {
        if (null == line)
            return (null);
        if (!line.contains(":"))
            return (null);
        return (line.substring(line.indexOf(":") + 1));
    }

    public boolean isVersionSupported(String line) {
        String version = getVersion(line);
        boolean supported = false;
        for (String supportedVersion : SUPPORTED_VERSIONS) {
            if (supportedVersion.equalsIgnoreCase(version))
                supported = true;
        }
        return (supported);
    }
    
    /***
     * initialize all map data (relief, size, rooms)
     * @param fileContent the file content as String
     */
    private void initFromStringArray(List<String> fileContent) {
        if (!isVersionSupported(fileContent.get(fileContent.size() - 1)))
            return;
        initSize(fileContent);
        initReliefs(fileContent);
        if (getVersion(fileContent.get(fileContent.size() - 1)).equals("0.0.2")) {
        	// computing rooms from file
        	initRooms(fileContent);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

	
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * @return the humanTopLeft
	 */
	public Coordinates getHumanTopLeft() {
		return humanTopLeft;
	}

	/**
	 * @param humanTopLeft the humanTopLeft to set
	 */
	public void setHumanTopLeft(Coordinates humanTopLeft) {
		this.humanTopLeft = humanTopLeft;
	}

	/**
	 * @return the humanBottomRight
	 */
	public Coordinates getHumanBottomRight() {
		return humanBottomRight;
	}

	/**
	 * @param humanBottomRight the humanBottomRight to set
	 */
	public void setHumanBottomRight(Coordinates humanBottomRight) {
		this.humanBottomRight = humanBottomRight;
	}

}
