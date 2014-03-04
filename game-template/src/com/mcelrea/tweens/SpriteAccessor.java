package com.mcelrea.tweens;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite>
{
	//alpha -> alpha channel -> opacity
	public static final int ALPHA = 0;

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		if(tweenType == ALPHA)
		{
			returnValues[ALPHA] = target.getColor().a;
			return 1;//the number of things affected
		}
		return 0;
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		
		if(tweenType == ALPHA)
		{
			target.setColor(target.getColor().r, 
							target.getColor().g, 
							target.getColor().b, 
					        newValues[ALPHA]);
		}
		
	}

}
