package org.copains.spaceexplorer.tactical.views;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.tactical.events.VisibleMapBlock;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;
import org.copains.spaceexplorer.tactical.tools.MapViewEvents;
import org.copains.spaceexplorer.tactical.tools.MapViewHelper;
import org.copains.spaceexplorer.tactical.tools.PathFinder;
import org.copains.spaceexplorer.tactical.tools.MapViewEvents.MapViewMode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class MapView extends View {

	public static int TILE_SIZE = 60;
	
	private GestureDetector gestureDetector;
	
	public MapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MapView(Context context,AttributeSet atttributes) {
		super(context, atttributes);
		gestureDetector = new GestureDetector(context, gestureListener);
		MapViewEvents.getInstance().setContext(context);
		//invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("Space", "Canvas Height " +canvas.getHeight());
		Log.i("Space", "Canvas width " + canvas.getWidth());
        int minSize = Math.min(canvas.getHeight(), canvas.getWidth());
        if (minSize > 1400) {
            TILE_SIZE = 180;
        } else if (minSize > 1000) {
            TILE_SIZE = 120;
        }
		StarshipMap map = StarshipMap.getInstance();
		Coordinates display = MapViewHelper.getInstance(map, canvas).getDisplayAreaStart();
		MapViewEvents events = MapViewEvents.getInstance();
		events.setParentView(this);
		events.reinitVisibleMapEvents();
		CurrentMission mission = CurrentMission.getInstance();
		for (int y = 0 ; y < MapViewHelper.getVerticalTilesCount(canvas) ; y++) {
			for (int x = 0 ; x < MapViewHelper.getHorizontalTilesCount(canvas) ; x++) {
				RectF r = new RectF((x*TILE_SIZE), (y*TILE_SIZE), TILE_SIZE+(x*TILE_SIZE), TILE_SIZE+(y*TILE_SIZE));
				Paint paint = new Paint();
				paint.setStyle(Style.FILL);
				paint.setAntiAlias(true);
				int tile = map.getRelief(x+display.getX(), y+display.getY());
				Coordinates mapPos = new Coordinates(x+display.getX(), y+display.getY());
				events.addVisibleMapEvent((x*TILE_SIZE), (y*TILE_SIZE),
						TILE_SIZE+(x*TILE_SIZE), TILE_SIZE+(y*TILE_SIZE), mapPos);
				switch (tile) {
				case 0:
					paint.setARGB(255, 0, 0, 0);
					canvas.drawRect(r, paint);
					break;
				case 1:
					//paint.setARGB(255, 15, 150, 0);
					BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.floor);
					canvas.drawBitmap(drawable.getBitmap(), null, r, null);
					break;
				case 2:
					paint.setARGB(255, 150, 150, 100);
					canvas.drawRect(r, paint);
					break;
				case 9:
					paint.setARGB(255, 255, 0, 0);
					canvas.drawRect(r, paint);
					break;
				default:
						break;
				}
				// Now we have to draw items on map if needed
				// TODO: use pictures in place of rects
				LifeForm lf = mission.getLifeFormOnMap(mapPos);
				if (null != lf) {
					events.addLifeFormEvent((x*TILE_SIZE), (y*TILE_SIZE),
							TILE_SIZE+(x*TILE_SIZE), TILE_SIZE+(y*TILE_SIZE), lf);
					//paint.setARGB(255, 250, 50, 250);
					//canvas.drawRect(r, paint);
					BitmapDrawable drawable = (BitmapDrawable)getResources().getDrawable(R.drawable.marine);
					canvas.drawBitmap(drawable.getBitmap(), null, r, null);
				}
			}
		}
		if (!mission.isTeamInPosition()) {
			displayTeamPositioning(canvas);
			MapViewEvents.getInstance().setViewMode(MapViewMode.TEAM_POSITIONING);
		}
		if (events.getViewMode() == MapViewMode.ACTION_HIGLIGHT) {
			PathFinder.getInstance().drawHighlight(canvas);
		}
		//canvas.save();
	}
	
	private void displayTeamPositioning(Canvas canvas) {
		int w;
		MapViewEvents events = MapViewEvents.getInstance();
		w = canvas.getWidth();
		RectF r = new RectF(0, 0, w, (TILE_SIZE*2)+15);
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setARGB(100, 0, 0, 250);
		canvas.drawRect(r, paint);
		int delta = (w-3*TILE_SIZE) / 4;
		for (int y = 0 ; y < 2 ; y++) {
			for (int x = 0 ; x < 3 ; x++) {
				CurrentMission mission = CurrentMission.getInstance();
				LifeForm teamMember = mission.getTeamMember(x+y*3);
				if (teamMember.getPosX() == -1) {
					r = new RectF(delta + x*(delta+TILE_SIZE) ,5+y*(TILE_SIZE+5),delta + x*(delta+TILE_SIZE)+TILE_SIZE,5+y*(TILE_SIZE+5)+TILE_SIZE);
					Log.i("spaceexplorers","Position pour x,y : (" + x + "," + y + ") : " +  r.toShortString());
					paint.setARGB(255, 250, 50, 250);
					canvas.drawRect(r, paint);
					events.addPositioningEvent(delta + x*(delta+TILE_SIZE) ,
							5+y*(TILE_SIZE+5),delta + x*(delta+TILE_SIZE)+TILE_SIZE,
							5+y*(TILE_SIZE+5)+TILE_SIZE, x+y*3);
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
        /*int action = event.getAction();
        switch (action) {
        	case MotionEvent.ACTION_DOWN:
        		Log.i("Space","action down");
        		break;
        	case MotionEvent.ACTION_MOVE:
        		Log.i("Space","action move");
        		break;
        	case MotionEvent.ACTION_UP:
        		Log.i("Space","action up");
        		break;
        }*/
		return (true);
	}
	
	private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
		
		float totalX = 0, totalY = 0;
		
		public boolean onScroll(android.view.MotionEvent event1, android.view.MotionEvent event2, float distanceX, float distanceY) {
			totalX += distanceX;
			totalY += distanceY;
			int moveX = 0, moveY = 0;
			boolean shouldMove = false;
			if (Math.abs(totalX) > TILE_SIZE) {
				moveX = (int)totalX / TILE_SIZE;
				totalX %= TILE_SIZE;
				shouldMove = true;
			}
			if (Math.abs(totalY) > TILE_SIZE) {
				moveY = (int)totalY / TILE_SIZE;
				totalY %= TILE_SIZE;
				shouldMove = true;
			}
			if (shouldMove) {
				MapViewHelper.getInstance().move(moveX, moveY);
				invalidate();
			}
			return (shouldMove);
		};
		
		public boolean onSingleTapConfirmed(MotionEvent event) {
			MapViewEvents events = MapViewEvents.getInstance();
			if (events.checkEvent(event)) {
				invalidate();
			}
			return (false);
		};
	};
	
	@Override
	protected void onCreateContextMenu(android.view.ContextMenu menu) {
		menu.add("Fin de Tour");
	};

}
