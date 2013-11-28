import java.util.Stack;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public class BubbleController
{
  public static Image m_offscreen = null;
  private static Stack m_controller_stack = null;
  public static BubbleController m_mover = null;
  public boolean m_first = true;

  public static final BubbleController CurrentController()
  {
    if (m_controller_stack == null)
      return null;
    if (m_controller_stack.empty() == true)
      return null;
    return (BubbleController)m_controller_stack.peek();
  }

  public static final boolean IsEmptyController()
  {
    if (m_controller_stack == null)
      return true;
    return m_controller_stack.empty();
  }

  public static final void ClearControllerStack()
  {
    m_controller_stack.removeAllElements();
  }

  public static final byte ControllerStackCount()
  {
    return (byte)m_controller_stack.size();
  }

  public static final BubbleController GetController(int paramInt)
  {
    return (BubbleController)m_controller_stack.elementAt(paramInt);
  }

  public static final void InitClass()
  {
    m_controller_stack = new Stack();
    m_offscreen = BubbleSmile.m_img_off;
  }

  public static final void TermClass()
  {
    m_controller_stack.removeAllElements();
    m_controller_stack = null;
    m_offscreen = null;
    m_mover = null;
  }

  public static final BubbleController GetNormal()
  {
    if (m_controller_stack == null)
      return null;
    if (m_controller_stack.empty())
      return null;
    return (BubbleController)m_controller_stack.elementAt(0);
  }

  public void Draw(Graphics paramGraphics, BubbleGame paramBubbleGame)
  {
  }

  public void KeyPressed(BubbleGame paramBubbleGame, int paramInt)
  {
  }

  public void Save(RecordStore paramRecordStore)
  {
  }

  public void Load(RecordStore paramRecordStore, int paramInt)
  {
  }

  public int DelayTimeEntry()
  {
    if (this.m_first)
    {
      this.m_first = false;
      return DelayTimeFirst();
    }
    return DelayTime();
  }

  public int DelayTimeFirst()
  {
    return 0;
  }

  public int DelayTime()
  {
    return 300;
  }

  public boolean IsRepaint()
  {
    return false;
  }

  public void SetActive(boolean paramBoolean)
  {
    if (m_controller_stack == null)
      return;
    if (paramBoolean == true)
      m_controller_stack.push(this);
    else
      m_controller_stack.pop();
  }

  public void Repaint(BubbleGame paramBubbleGame)
  {
    paramBubbleGame.repaint();
  }
}