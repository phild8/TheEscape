package edu.orangecoastcollege.escapethecatcher;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a 2d game with two objects: a player object and AI zombie object. The player must avoid
 * obstacles and make it to the exit in order to win. Once the player makes it the exit a win is
 * tallied and the zombie is turned into a bunny. If zombie catches the player. The player is
 * turned into a blood splatter and a lose is tallied. The game will restart it is a lose or a win.
 */
public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 130;
    final int OFFSET = 5;
    final int COLS = 8;
    final int ROWS = 12;
    final int gameBoard[][] = {
            {1, 1, 1, 1, 1, 1, 1, 1}, // 1
            {1, 2, 2, 1, 2, 2, 1, 1}, // 2
            {1, 2, 2, 2, 2, 2, 2, 1}, // 3
            {1, 2, 1, 2, 2, 2, 2, 1}, // 4
            {1, 2, 2, 2, 2, 2, 1, 1}, // 5
            {1, 2, 2, 1, 2, 2, 2, 3}, // 6
            {1, 2, 1, 2, 2, 2, 2, 1}, // 7
            {1, 2, 1, 2, 2, 1, 2, 1}, // 8
            {1, 2, 2, 2, 2, 2, 2, 1}, // 9
            {1, 2, 1, 2, 2, 2, 1, 1}, // 10
            {1, 2, 2, 2, 1, 2, 2, 1}, // 11
            {1, 1, 1, 1, 1, 1, 1, 1}  // 12
    };
    private List<ImageView> allGameObjects;
    private Player player;
    private Zombie zombie;

    //LAYOUT AND INTERACTIVE INFORMATION
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private ImageView bloodImageView;
    private ImageView bunnyImageView;
    private TextView winsTextView;
    private TextView lossesTextView;
    private Handler handler;

    private boolean endOfRound; // Disables gesture detector

    private int exitRow;
    private int exitCol;

    //  WINS AND LOSSES
    private int wins;
    private int losses;

    private LayoutInflater layoutInflater;

    /**
     * Initializes the objects of the game and then starts the game.
     * @param savedInstanceState Loads a saved instance if there is one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        handler = new Handler();

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        allGameObjects = new ArrayList<>();

        // Instantiate our gesture detector
        gestureDetector = new GestureDetector(this, this);

        startNewGame();
    }

    /**
     * Restarts the game by returning the zombie and player into their original positions.
     */
    private void startNewGame() {
        /*
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for (int i = 0; i < allGameObjects.size(); i++) {
            ImageView visualObj = allGameObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        */
        for(ImageView iv : allGameObjects)
            activityGameRelativeLayout.removeView(iv);
        allGameObjects.clear();

        //TASK 2:  REBUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE PLAYERS
        createZombie();
        createPlayer();

        endOfRound = true;
        Toast.makeText(this, "Turn the zombie into a harmless bunny by escaping the maze.", Toast.LENGTH_SHORT).show();

        winsTextView.setText(getString(R.string.wins, wins));
        lossesTextView.setText(getString(R.string.losses, losses));
    }

    /**
     * Creates a GUI of all the objects that have been initialized and added to list and layout.
     */
    private void buildGameBoard() {
        // TODO: Inflate the entire game board (obstacles and exit)
        // TODO: (everything but the player and zombie)

        ImageView viewToInflate;
        // Loop through the board:
        for (int row = 0; row < ROWS; row++){
            for (int col = 0; col < COLS; col++){
                viewToInflate = null;
                if (gameBoard[row][col] == BoardCodes.OBSTACLE){
                    viewToInflate = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout, null);
                }
                else if (gameBoard[row][col] == BoardCodes.EXIT){
                    viewToInflate = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    exitRow = row;
                    exitCol = col;
                }

                if (viewToInflate != null){
                    // SET the x and y position of the viewToInflate
                    viewToInflate.setX(col * SQUARE + OFFSET);
                    viewToInflate.setY(row * SQUARE + OFFSET);
                    // Add the view to the relative layout and list of ImageViews
                    activityGameRelativeLayout.addView(viewToInflate);
                    allGameObjects.add(viewToInflate);
                }
            }
        }

    }

    /**
     * Creates the zombie AI object and ImageView. Adds the object to the a list and layout.
     */
    private void createZombie() {
        // TODO: Determine where to place the Zombie (at game start)
        // TODO: Then, inflate the zombie layout
        int row = 2;
        int col = 4;
        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout, null);
        zombieImageView.setX(col * SQUARE + OFFSET);
        zombieImageView.setX(row * SQUARE + OFFSET);
        // Add to relative layout and the list
        activityGameRelativeLayout.addView(zombieImageView);
        allGameObjects.add(zombieImageView);
        // Instantiate the zombie
        zombie = new Zombie();
        zombie.setRow(row);
        zombie.setCol(col);
    }

    /**
     * Creates the zombie AI object and ImageView. Adds the object to the a list and layout.
     */
    private void createPlayer() {
        // TODO: Determine where to place the Player (at game start)
        // TODO: Then, inflate the player layout
        int row = 4;
        int col = 2;
        playerImageView = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        playerImageView.setX(col * SQUARE + OFFSET);
        playerImageView.setY(row * SQUARE + OFFSET);
        // Add to relative layout and the list
        activityGameRelativeLayout.addView(playerImageView);
        allGameObjects.add(playerImageView);
        // Instantiate the zombie
        player = new Player();
        player.setRow(row);
        player.setCol(col);
    }

    /**
     * Moves the player up, down, left, and right inside of a 2d array which represents the GUI.
     * @param velocityX The row index of the player.
     * @param velocityY The column index of the player.
     */
    private void movePlayer(float velocityX, float velocityY) {
        // TODO: This method gets called by the onFling event
        // TODO: Be sure to implement the move method in the Player (model) class

        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);

        String direction = "UNKNOWN";
        if (absX >= absY){
            if (velocityX < 0)
                direction = "LEFT";
            else
                direction = "RIGHT";
        } else {
            if (velocityY < 0)
                direction = "UP";
            else
                direction = "DOWN";
        }
        if (!direction.equals("UNKNOWN")){
            player.move(gameBoard, direction);
            // Move the image view as well
            playerImageView.setX(player.getCol() * SQUARE + OFFSET);
            playerImageView.setY(player.getRow() * SQUARE + OFFSET);

            zombie.move(gameBoard, player.getCol(), player.getRow());
            zombieImageView.setX(zombie.getCol() * SQUARE + OFFSET);
            zombieImageView.setY(zombie.getRow() * SQUARE + OFFSET);
        }

        // Make 2 decisions
        // 1) Check to see if Player has reached the exit row and col
        // 2) OR if the Player and Zombie are touching  (LOSE)
        if (player.getCol() == exitCol - 1  && player.getRow() == exitRow) {
            endOfRound = false; // Stops player and zombie from moving once round is over

            bunnyImageView = (ImageView) layoutInflater.inflate(R.layout.bunny_layout, null);
            bunnyImageView.setX(zombie.getCol() * SQUARE + OFFSET);
            bunnyImageView.setY(zombie.getRow() * SQUARE + OFFSET);

            activityGameRelativeLayout.removeView(zombieImageView);
            allGameObjects.remove(zombie);
            activityGameRelativeLayout.removeView(playerImageView);
            allGameObjects.remove(player);

            activityGameRelativeLayout.addView(bunnyImageView);

            ++wins;
            Toast.makeText(this, "You have won this round!", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNewGame();
                }
            }, 2000);
        }
        else if (player.getCol() == zombie.getCol() && player.getRow() == zombie.getRow()) {
            endOfRound = false; // Stops player and zombie from moving once round is over

            bloodImageView = (ImageView) layoutInflater.inflate(R.layout.blood_layout, null);
            bloodImageView.setX(player.getCol() * SQUARE + OFFSET);
            bloodImageView.setY(player.getRow() * SQUARE + OFFSET);

            activityGameRelativeLayout.removeView(playerImageView);
            allGameObjects.remove(player);

            activityGameRelativeLayout.addView(bloodImageView);

            ++losses;
            Toast.makeText(this, "You have lost this round.", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNewGame();
                }
            }, 2000);
        }

        // TODO: Determine which absolute velocity is greater (x or y)
        // TODO: If x is negative, move player left.  Else if x is positive, move player right.
        // TODO: If y is negative, move player down.  Else if y is positive, move player up.

        // TODO: Then move the zombie, using the player's row and column position.
    }

    /**
     * Overrides the defined implementation and calls the user gestureDetector instead.
     * @param event The motion even that was detected
     * @return The motion event.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event); // defined listener instead of super
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     * Moves the player up, down, left, or right depending on the direction of the fling.
     * @param motionEvent Detects motion on the screen.
     * @param motionEvent1 Detects motion on the screen.
     * @param v Starting position of fling.
     * @param v1 End position of fling.
     * @return true
     */
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (endOfRound)
            movePlayer(v, v1);

        return true;
    }
}
