package bin;

/**
 * Created by Sajid on 2016-06-06.
 */
import java.awt.*;

/**
 * Created by Sajid on 2016-05-02.
 */
//Displays 2 lines of text by scrolling letter by letter
//the text can also be skipped line by line

public class TextBoxState extends GameState{

    //properties of textbox
    private String[] text;  //all of the text to be displayed
    private int totLines;
    private int currLine;
    private boolean goNextLine;
    private Color textColor;
    private Color boxColor;
    private Color borderColor;
    private int x,y,h,w;
    private String currText;    //current text being scrolled
    private String firstLineText;   //the text being displayed on the first line when current text is on the 2nd line
    private int lineSize;
    private int delay;
    private int letterIndex;
    private int textSize;
    private boolean pressedDown;

    //initializes properties
    public TextBoxState(Player player,StateType stateType,String[] text, Color textColor,Color boxColor,Color borderColor,int x,int y,int w, int h, int textSize){
        super(player,stateType);
        this.text = text;
        this.textColor = textColor;
        this.boxColor = boxColor;
        this.borderColor = borderColor;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.textSize = textSize;
        totLines = text.length;
        currText = "";
        firstLineText = "";
        delay = 0;
        currLine = 0;
        lineSize = text[currLine].length();
        letterIndex = 0;
        goNextLine = true;
        pressedDown = false;
    }

    public void drawBox(Graphics2D g){
        g.setColor(borderColor);
        g.fillRect(x - 5, y - 5, w + 10, h + 10);
        g.setColor(boxColor);
        g.fillRect(x,y,w,h);
    }

    //renders the box, border and line(s) of text
    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        drawBox(g2);
        g.setColor(textColor);
        g.setFont(new Font("Eras Bold ITC", Font.BOLD, textSize));
        int line = currLine%2; //1st or 2nd text line
        g.drawString(currText,x + w/30, (y + h/3)+line*h/2);
        if(line == 1){
            g.drawString(firstLineText,x + w/30, (y + h/3));
        }
    }

    public void tick(){
        //only ticks if the current two lines aren't filled for the textbox
        if(!goNextLine){
            return;
        }
        delay++;
        if(delay % 2 == 0){
            //goes through each letter and adds to the current text being displayed
            if(letterIndex < lineSize){
                currText += text[currLine].substring(letterIndex,letterIndex+1);
                letterIndex++;
            }
            //otherwise goes to the second line of text on screen (only occurs from transitioning from 1st to 2nd line)
            else if(currLine%2 == 0 && currLine!= totLines-1){
                firstLineText = currText; //the text written becomes the first line of text to be rendered
                //resets all the text variables and goes to next line
                currText = "";
                currLine++;
                letterIndex = 0;
                lineSize = text[currLine].length();
            }
        }
    }

    public void proceed(){
        //completes the line it was on if prompted before line is
        if(letterIndex < lineSize && (currLine-1) != totLines){
            currText = text[currLine];
            letterIndex = lineSize;
            return;
        }
        currLine++;
        //when its the last line of text, text box ends
        if(currLine == totLines){
            GameStateManager.removeState(this);
            currLine--;
        }
        else{
            //goes to next two line screenings, and resets the current text being rendered
            goNextLine = true;
            currText = "";
            firstLineText = currText; //first line text is first line when current line is 2nd line
            letterIndex = 0;
            lineSize = text[currLine].length();
        }
    }

    //keeps going to next line as long as key is let go
    public void keyZPressed(){
        if(!pressedDown){
            proceed();
            pressedDown = true;
        }
    }
    //resets the properties of the textbox for when its used again
    public void reset(){
        currText = "";
        delay = 0;
        currLine = 0;
        lineSize = text[currLine].length();
        letterIndex = 0;
        goNextLine = true;
    }

    public void noKeyZPressed(){
        pressedDown = false;
    }
}