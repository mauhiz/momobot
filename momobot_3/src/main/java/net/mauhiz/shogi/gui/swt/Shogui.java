package net.mauhiz.shogi.gui.swt;

import java.awt.Point;

import net.mauhiz.irc.gui.actions.ExitAction;
import net.mauhiz.shogi.model.Board;
import net.mauhiz.shogi.model.OwnedPiece;
import net.mauhiz.shogi.model.Piece;
import net.mauhiz.shogi.model.Player;
import net.mauhiz.shogi.model.Rule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
// TODO SquaresIterator
public class Shogui {
    
    private static final Shogui INSTANCE = new Shogui();
    
    public static void cancelSelection() {
        INSTANCE.selectedSquare = null;
        INSTANCE.selectedPiece = null;
        redraw();
    }
    
    public static void main(String... args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        shell.setSize(400, 400);
        shell.setMinimumSize(400, 400);
        
        /* menu */
        INSTANCE.setShell(shell);
        INSTANCE.initMenu();
        
        shell.open();
        shell.pack();
        // SWT loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
    
    public static void movePiece(Point to) {
        INSTANCE.doMovePiece(to);
    }
    
    public static void redraw() {
        INSTANCE.refreshBoard();
    }
    
    public static void selectPiece(Piece inPocket) {
        INSTANCE.selectedPiece = inPocket;
        redraw();
    }
    
    public static void selectPiece(Point at) {
        INSTANCE.selectedSquare = at;
        redraw();
    }
    
    public static void start() {
        INSTANCE.initBoard();
        INSTANCE.b.newGame();
        redraw();
    }
    private final Board b = new Board();
    
    final Button[][] buttons = new Button[Board.SIZE][Board.SIZE];
    private final SelectionListener[][] listeners = new SelectionListener[Board.SIZE][Board.SIZE];
    private Piece selectedPiece;
    private Point selectedSquare;
    
    private Shell shell;
    
    private void disableButton(int i, int j) {
        Button button = buttons[i][j];
        Color fore = button.getForeground();
        Color back = button.getBackground();
        SelectionListener action = listeners[i][j];
        if (action != null) {
            button.removeSelectionListener(action);
        }
        listeners[i][j] = null;
        button.setEnabled(false);
        button.setForeground(fore);
        button.setBackground(back);
    }
    
    private void displayPockets() {
        // TODO pocket bar for W
        // TODO pocket bar for b
        // TODO select actions on pocket
    }
    
    private void doMovePiece(Point to) {
        if (selectedSquare != null) {
            if (b.move(b.getTurn(), selectedSquare, to)) {
                OwnedPiece currentPiece = b.getOwnedPieceAt(to);
                
                if (Rule.canPromote(currentPiece, selectedSquare, to)) {
                    MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                    mb.setMessage("Promote?");
                    int buttonID = mb.open();
                    switch (buttonID) {
                        case SWT.YES :
                            currentPiece.promoted = true;
                            break;
                        default :
                            break;
                    }
                }
                selectedSquare = null;
                refreshBoard();
            }
        } else if (selectedPiece != null) {
            if (b.drop(b.getTurn(), selectedPiece, to)) {
                selectedPiece = null;
                refreshBoard();
            }
        }
    }
    
    private void enableButton(int i, int j, SelectionListener action) {
        Button button = buttons[i][j];
        Color fore = button.getForeground();
        Color back = button.getBackground();
        button.addSelectionListener(action);
        listeners[i][j] = action;
        button.setEnabled(true);
        button.setForeground(fore);
        button.setBackground(back);
    }
    
    private void initBoard() {
        
        /* layout */
        GridLayout gridLayout = new GridLayout(Board.SIZE, true);
        shell.setLayout(gridLayout);
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        
        Color gray = shell.getDisplay().getSystemColor(SWT.COLOR_GRAY);
        
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                int x = j;
                int y = Board.SIZE - i - 1;
                Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
                buttons[x][y] = button;
                button.setBackground(gray);
            }
        }
        
        shell.addPaintListener(new PaintListener() {
            
            @Override
            public void paintControl(PaintEvent arg0) {
                for (int i = 0; i < Board.SIZE; i++) {
                    for (int j = 0; j < Board.SIZE; j++) {
                        buttons[i][j].setSize(30, 30);
                    }
                }
            }
        });
    }
    
    private void initMenu() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        
        MenuItem fileStartItem = new MenuItem(fileMenu, SWT.PUSH);
        fileStartItem.setText("New &Game");
        fileStartItem.addSelectionListener(new StartAction());
        
        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");
        fileExitItem.addSelectionListener(new ExitAction(shell));
        shell.setMenuBar(menuBar);
    }
    
    private void refreshBoard() {
        Display display = shell.getDisplay();
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        shell.setRedraw(false);
        
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                Point targetPoint = new Point(i, j);
                OwnedPiece op = b.getOwnedPieceAt(targetPoint);
                Button button = buttons[i][j];
                disableButton(i, j);
                
                if (selectedSquare != null) { // from the board
                    // available destinations
                    OwnedPiece selected = b.getOwnedPieceAt(selectedSquare);
                    if (!targetPoint.equals(selectedSquare) && Rule.canGo(b, selected, selectedSquare, targetPoint)) {
                        enableButton(i, j, new MoveAction(targetPoint));
                    }
                } else if (selectedPiece != null) { // from the pocket
                    if (Rule.canDrop(b, targetPoint)) {
                        enableButton(i, j, new MoveAction(targetPoint));
                    }
                } else {
                    // available pieces
                    if (op != null && op.player == b.getTurn()) {
                        enableButton(i, j, new SelectAction(targetPoint));
                    }
                }
                
                if (op == null) {
                    button.setText("");
                } else {
                    button.setText(op.getSymbol());
                    button.setForeground(op.player == Player.BLACK ? black : white);
                }
            }
        }
        if (selectedSquare != null) {
            enableButton(selectedSquare.x, selectedSquare.y, new CancelAction());
        }
        
        displayPockets();
        shell.setRedraw(true);
    }
    
    private void setShell(Shell shell) {
        this.shell = shell;
    }
}
