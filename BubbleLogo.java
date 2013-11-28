import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class BubbleLogo extends Canvas
{
  private static final byte MENU_COUNT = 5;
  public static final int CANVAS_W = 128;
  public static final int CANVAS_H = 128;
  private static final byte PIN_IMAGE_W = 5;
  private static final byte PIN_IMAGE_H = 5;
  private static final byte PIN_VERT_OFFSET_X = 5;
  private static final byte PIN_VERT_OFFSET_Y = 20;
  private static final byte PIN_HORZ_OFFSET_X = 20;
  private static final byte PIN_HORZ_OFFSET_Y = 5;
  private static final byte LOGO_OFFSET_Y = 10;
  private static final byte PIN_HORZ_CNT = 4;
  private static final byte PIN_VERT_CNT = 5;
  private BubbleSmile m_bs = null;
  private Image m_logo = null;
  private Image m_img_menu = null;
  private Image m_img_back = null;
  private boolean m_continue_flag = false;
  private int m_cur_sel = 0;

  public BubbleLogo(BubbleSmile paramBubbleSmile)
  {
    try
    {
      this.m_logo = Image.createImage("/logomain.png");
      this.m_img_menu = Image.createImage("/logomain_menu.png");
      this.m_img_back = BubbleSmile.m_img_off;
    }
    catch (IOException localIOException)
    {
    }
    this.m_continue_flag = BubbleGame.IsSaved();
    this.m_bs = paramBubbleSmile;
    BubbleSmile.PlaySound(5);
  }

  public void paint(Graphics paramGraphics)
  {
    Graphics localGraphics = this.m_img_back.getGraphics();
    localGraphics.drawImage(this.m_logo, 0, 0, 20);
    int i = 64;
    int j = 21;
    int k = 70;
    int l = 0;
    switch (this.m_cur_sel)
    {
    case 0:
      j = 25;
      k = 57;
      break;
    case 1:
      j = 25;
      k = 71;
      break;
    case 2:
      j = 25;
      k = 85;
      break;
    case 3:
      j = 25;
      k = 100;
      break;
    case 4:
      j = 25;
      k = 114;
    }
    localGraphics.setClip(j, k, 79, 12);
    localGraphics.drawImage(this.m_img_menu, j, k - this.m_cur_sel * 12, 20);
    paramGraphics.drawImage(this.m_img_back, 0, 0, 20);
  }

  protected void keyPressed(int paramInt)
  {
    switch (paramInt)
    {
    case -3:
    case -1:
    case 50:
    case 52:
      BubbleSmile.PlaySound(7);
      this.m_cur_sel -= 1;
      if (this.m_cur_sel < 0)
        this.m_cur_sel = 4;
      break;
    case -4:
    case -2:
    case 54:
    case 56:
      BubbleSmile.PlaySound(7);
      this.m_cur_sel += 1;
      if (this.m_cur_sel >= 5)
        this.m_cur_sel = 0;
      break;
    case -5:
    case 53:
      BubbleSmile.PlaySound(4);
      LuanchProgram(this.m_cur_sel);
      break;
    case -8:
    case -7:
    case 35:
      this.m_bs.notifyDestroyed();
      return;
    default:
      return;
    }
    super.repaint();
  }

  public void Term(boolean paramBoolean)
  {
    this.m_logo = null;
    this.m_img_menu = null;
    this.m_img_back = null;
    if (paramBoolean == true)
      this.m_bs = null;
    System.gc();
  }

  public void LuanchProgram(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      Term(false);
      this.m_bs.SetCurrent(new BubbleGameSelect(this.m_bs));
      break;
    case 1:
      if (!this.m_continue_flag)
        return;
      BubbleGameMode localBubbleGameMode = new BubbleGameMode();
      localBubbleGameMode.InitMode((paramInt == 0) ? 1 : 2);
      Term(false);
      this.m_bs.SetCurrent(new BubbleGame(this.m_bs, localBubbleGameMode, true));
      break;
    case 2:
      Term(false);
      this.m_bs.SetCurrent(new BubbleHighScore(this.m_bs, -1));
      break;
    case 3:
      Term(false);
      this.m_bs.SetCurrent(new BubbleHowToPlay(this.m_bs));
      break;
    case 4:
      Term(false);
      this.m_bs.SetCurrent(new BubbleOption(this.m_bs));
    }
  }
}