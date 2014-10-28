package com.hifreshday.android.pge.widget;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.hifreshday.android.pge.engine.options.EngineOptions;
import com.hifreshday.android.pge.entity.shape.sprite.Sprite;
import com.hifreshday.android.pge.view.res.IBitmapRes;

public abstract class TextMarqueeSprite extends Sprite {
	
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 40;
	public static final int DEFAULT_SPEED = 50;

	private int marqueeWidth = DEFAULT_WIDTH;
	private int marqueeHeight = DEFAULT_HEIGHT;

	private int speed = DEFAULT_SPEED;
	private int currentTextPosition = marqueeWidth;
	private float buffersecond = 0.0f;
	private String info;
	private float stringsize = 0.0f;
	private String temp = null;
	
	private Paint paint;
	private int startX;
	private int startY;
	
	public TextMarqueeSprite(IBitmapRes bitmapRes, int pX, int pY, int width,
			int height, int marqueeWidth, int marqueeHeight, int speed) {
		super(bitmapRes, pX, pY, width, height);
		setVisible(false);
		if(marqueeWidth != 0){
			this.marqueeWidth = marqueeWidth;
			currentTextPosition = marqueeWidth;
		}
		if(marqueeHeight != 0){
			this.marqueeHeight = marqueeHeight;
		}
		if(speed != 0){
			this.speed = speed;
		}
	}
	
	/**
	 * 显示跑马灯效果
	 * @param text  要显示的文字
	 * @param startX  起始位置x坐标
	 * @param startY  起始位置y坐标
	 * @param paint   设置文字效果的paint
	 */
	public void showInfo(String text,int startX,int startY,Paint paint) {
		this.paint = paint;
		this.startX = startX;
		this.startY = startY;
		if (!isVisible()) {
			info = text;
			stringsize = paint.measureText(info);
			currentTextPosition = marqueeWidth;
			setVisible(true);
		} else {
			temp = text;
		}
	}

	@Override
	protected void onUpdateSelf(float secondsElapsed) {
		buffersecond += secondsElapsed;
		if ((buffersecond * speed) > 1) {
			int move = (int) (buffersecond * speed);
			currentTextPosition -= move;
			if (currentTextPosition + stringsize < 0) {
				currentTextPosition = marqueeWidth;
				setVisible(false);
				buffersecond = 0;
			}
			buffersecond = (buffersecond * speed - move) / speed;
		}
		if (null != temp && !isVisible()) {
			setVisible(true);
			info = temp;
			buffersecond += secondsElapsed;
			if ((buffersecond * speed) > 1) {
				int move = (int) (buffersecond * speed);
				currentTextPosition -= move;
				if (currentTextPosition + stringsize < 0) {
					currentTextPosition = marqueeWidth;
					setVisible(false);
					temp = null;
				}
				buffersecond = (buffersecond * speed - move) / speed;
			}
		}
	}

	@Override
	public void onDrawSelf(Canvas canvas) {
		canvas.save();
		canvas.translate(
				EngineOptions.getOffsetX() + startX
						* EngineOptions.getScreenScaleX(),
				EngineOptions.getOffsetY() + startY
						* EngineOptions.getScreenScaleY());
		canvas.clipRect(0, 0, marqueeWidth * EngineOptions.getScreenScaleX(),
				marqueeHeight * EngineOptions.getScreenScaleY());
		canvas.drawText(info,
				currentTextPosition * EngineOptions.getScreenScaleX(),
				startY * EngineOptions.getScreenScaleY(), paint);
		canvas.restore();
	}
	
}
