package tl.GUIutil;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import tl.Util.Cursor;

public class Container extends GUIControl
{
	private ArrayList<Item> items;
	private int numItems;
	private int numDown;
	private boolean toobig;
	private int iWidth;
	private int iHeight;
	private int gap;
	private boolean scaled;
	
	public Container()
	{
		super();
		priority = 0;
		type = GUIControl.ControlType.container;
		numItems = 0;
		numDown = 0;
		toobig = false;
		iWidth = 0;
		iHeight = 0;
		gap = 0;
		scaled = false;
		items = new ArrayList<Item>();
	}
	
	public Container(int x, int y, int w, int h, Color col) throws SlickException
	{
		super(x, y, w, h);
		priority = 0;
		type = GUIControl.ControlType.container;
		//colour = col;
		graphic = new Image(w, h);
		numItems = 0;
		numDown = 0;
		toobig = false;
		items = new ArrayList<Item>();
		iWidth = 30;
		iHeight = 30;
		gap = 20;
		scaled = false;
		changed = true;
	}
	
	public Container(int x, int y, int w, int h, int iw, int ih, int gap) throws SlickException
	{
		super(x, y, w, h);
		priority = 0;
		type = GUIControl.ControlType.container;
		graphic = new Image(w, h);
		numItems = 0;
		numDown = 0;
		toobig = false;
		items = new ArrayList<Item>();
		iWidth = iw;
		iHeight = ih;
		scaled = true;
		this.gap = gap;
		changed = true;
	}
	
	private void updateC() throws SlickException
	{
		canvas = graphic.getGraphics();
		canvas.clear();
		canvas.setColor(new Color(0, 0, 0));
		canvas.drawRect(0, 0, width - 1, height - 1);
		
		toobig = iHeight * numItems + gap > height ? true : false;
		double w = width;
		double iw = iWidth;
		double g = gap;
		double ni = numItems;
		int fitWidth = (int)Math.round(w / ((iw + g) * (ni > 0 ? ni : 1))); // how many can fit horizontally
		
		for (int i = 0; i < numItems; i += fitWidth)
		{
			for (int n = i; n < i + fitWidth; n++)
			{
				int j = 0;
				if (numDown + i < numItems)
				{
					if (items.get(n) != null)
						canvas.drawImage(items.get(n).iGraphic, gap + j * iWidth, i * (iHeight + gap) + gap);
				}
				j++;
			}
		}
		
		if (toobig)
		{
			canvas.drawLine(width - 13, height / 2, width - 1, height / 2);
			canvas.drawLine(width - 14, 1, width - 14, height);
			Polygon up = new Polygon();
			up.addPoint(width - 12, 10);
			up.addPoint(width - 2, 10);
			up.addPoint(width - 7, 2);
			canvas.draw(up);
			Polygon down = new Polygon();
			down.addPoint(width - 12, height - 10);
			down.addPoint(width - 2, height - 10);
			down.addPoint(width - 7, height - 2);
			canvas.draw(down);
			canvas.fill(up);
			canvas.fill(down);
		}
	}
	
	public void update(Graphics g)
	{
		graphic.setAlpha(owningGUI.graphic.getAlpha());
		
		if (mouseButtonDown(0))
		{
			float y = Cursor.getY() - gy;
			float x = Cursor.getX() - gx;

			if (toobig)
			{
				if (x >= width - 12 && x <= width - 2 && y <= 10 && y >= 2)
				{
					if (numDown - 1 >= 0)
					{
						numDown--;
						changed = true;
					}
				}
				if (x >= width - 12 && x <= width - 2 && y >= height - 10 && y <= height - 2)
				{
					if (numDown + 1 < numItems)
					{
						numDown++;
						changed = true;
					}
				}
			}
			/*
			if (x >= this.x && x < this.x + (width - 12))
			{
				int gapSize = 25;
				int where = (int)(y / gapSize) + numDown;
				selected = (where < numItems ? where : -1);
			}
			*/
		}
		
		try
		{
			if (changed)
			{
				updateC();
				changed = false;
			}
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
		
		if (visible && graphic.getAlpha() > 0.00F)
			g.drawImage(graphic, gx, gy);
	}
	
	private class Item
	{
		public Image iGraphic;
		
		public Item(Image i)
		{
			iGraphic = scaled ? i.getScaledCopy(iWidth, iHeight) : i;
		}
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
	
	public void addItem(Image image)
	{
		items.add(new Item(image));
		numItems++;
		changed = true;
	}
	
	public void addItemAt(int pos, Image image)
	{
		items.add(pos, new Item(image));
		numItems++;
		changed = true;
	}
	
	public void setItem(int pos, Image image)
	{
		items.set(pos, new Item(image));
		changed = true;
	}
	
	public void removeItem(int pos)
	{
		items.remove(pos);
		numItems--;
		if (!toobig)
			numDown = 0;
		changed = true;
	}
	
	public Image getGraphic(int pos)
	{
		return items.get(pos).iGraphic;
	}
	
	public void clear()
	{
		items.clear();
		numItems = 0;
		changed = true;
	}
}