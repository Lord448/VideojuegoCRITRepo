package ca.crit.hungryhamster.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.crit.hungryhamster.GameHandler;

public class Animal {
    private int width = 7, height = 10;
    private float x, y;
    private final float speed;
    private final float[] positions = new float[GameHandler.numHouseSteps];
    private final Texture animal_texture;
    private int nextPin = 0;
    private final Sound victorySound;
    public Animal (int x, int y, int width, int height, int max_lim, int min_lim, float speed) {
        float positionSet = 0;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        GameHandler.foodPositions = new float[GameHandler.numHouseSteps];
        GameHandler.animalPositions = new float[GameHandler.numHouseSteps];
        GameHandler.foodSaved = max_lim;
        animal_texture = new Texture("Animals/cutiehamster.png");
        victorySound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Effects/achievement.ogg"));
        //Each position has a step of 7.5 units when we have a length of 8 positions
        for(int i = 0; i < positions.length; i++) {
            positionSet += ((float) (max_lim - min_lim) / positions.length);
            positions[i] = positionSet;
            GameHandler.foodPositions[i] = positions[i];
            System.out.println("Pos:" + i + " " + positions[i]);
        }
    }
    public void render(final SpriteBatch batch){
        batch.draw(animal_texture, x, y, width, height);
        if(GameHandler.environment == GameHandler.DESKTOP_ENV)
            checkKeyPressed(); //Only for desktop environment
        climb();
    }

    private void checkKeyPressed(){
        int currentPin;
        for(int i = 0; i < GameHandler.numHouseSteps; i++) {
            if(Gdx.input.isKeyJustPressed(GameHandler.key[i])) {
                currentPin = i;
                if(currentPin == nextPin){
                    GameHandler.touchPins[i] = true;
                    nextPin++;
                }
                for(int j = 0; j < GameHandler.numHouseSteps; j++) {
                    if(j != i)
                        GameHandler.touchPins[j] = false;
                }
            }
        }
    }

    private void climb(){
        float currentPos = y;
        //Controls the move of the animal
        for(int i = 0; i < GameHandler.numHouseSteps; i++) {
            if(GameHandler.touchPins[i]) { //Searching if we need to move the animal
                if(currentPos > positions[i]+GameHandler.animHysteresis) { //Moving down
                    y -= Gdx.graphics.getDeltaTime()*speed;
                }
                else if(currentPos < positions[i]-GameHandler.animHysteresis){ //Moving up
                    y += Gdx.graphics.getDeltaTime()*speed;
                    //Checking if we have reached the position
                    if(y >= positions[GameHandler.animalCounter+1] - GameHandler.animHysteresis) {
                        GameHandler.foodPicked = true;
                        GameHandler.animalPositions[i] = y;
                        GameHandler.animalCounter++;
                        if(GameHandler.animalCounter == GameHandler.countsToWin) { //Finish the session
                            winSound();
                        }
                    }
                }
            }
        }
    }

    private void winSound() {
        long id = victorySound.play(GameHandler.musicVolume);
        victorySound.setPitch(id, 1);
        victorySound.setLooping(id, false);
    }

    public void dispose(){
        victorySound.dispose();
    }
}
