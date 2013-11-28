import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class BubbleGameSelect extends Canvas
{
  private Image m_img = null;
  private Image m_sel_on = null;
  private BubbleSmile m_bs = null;
  private boolean m_flag = false;

  public BubbleGameSelect(BubbleSmile paramBubbleSmile)
  {
    this.m_bs = paramBubbleSmile;
    try
    {
      Image localImage = Image.createImage("/logomain.png");
      this.m_img = BubbleSmile.m_img_off;
      Graphics localGraphics = this.m_img.getGraphics();
      localGraphics.drawImage(localImage, 0, 0, 20);
      localImage = null;
      localImage = Image.createImage("/gamesel.png");
      localGraphics.drawImage(localImage, 21, 57, 20);
      localImage = null;
      System.gc();
      this.m_sel_on = Image.createImage("/gameselon.png");
    }
    catch (IOException localIOException)
    {
    }
  }

  public void paint(Graphics paramGraphics)
  {
    paramGraphics.drawImage(this.m_img, 0, 0, 20);
    if (!this.m_flag)
    {
      paramGraphics.setClip(21, 67, 87, 14);
      paramGraphics.drawImage(this.m_sel_on, 21, 67, 20);
    }
    else
    {
      paramGraphics.setClip(21, 89, 87, 14);
      paramGraphics.drawImage(this.m_sel_on, 21, 75, 20);
    }
  }

  public void keyPressed(int paramInt)
  {
    switch (paramInt)
    {
    case -2:
    case -1:
    case 50:
    case 56:
      BubbleSmile.PlaySound(7);
      this.m_flag = (!this.m_flag);
      super.repaint();
      break;
    case -5:
    case 53:
      BubbleSmile.PlaySound(4);
      Final(true);
      break;
    case -8:
    case -7:
    case 35:
      Final(false);
    }
  }

  public void Final(boolean paramBoolean)
  {
    this.m_img = null;
    this.m_sel_on = null;
    System.gc();
    if (paramBoolean == true)
    {
      BubbleGameMode localBubbleGameMode = new BubbleGameMode();
      localBubbleGameMode.InitMode((!this.m_flag) ? 1 : 2);
      this.m_bs.SetCurrent(new BubbleGame(this.m_bs, localBubbleGameMode, false));
    }
    else
    {
      this.m_bs.SetCurrent(new BubbleLogo(this.m_bs));
    }
    this.m_bs = null;
    System.gc();
  }
}