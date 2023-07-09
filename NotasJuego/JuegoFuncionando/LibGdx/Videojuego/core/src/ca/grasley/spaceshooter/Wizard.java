package ca.grasley.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wizard {
    private int width, height;
    private float spellingSpeed;
    public int x, y;
    /*ANIMATION*/
    private Animation spellingAnimation;
    private Animation idleAnimation;
    private float spellingTime, time;
    /*TEXTURES*/
    private TextureRegion [] spellingMovement;
    private TextureRegion [] idleMovement;
    private Texture image;
    private TextureRegion currentSpelling_Frame;
    private TextureRegion currentIdle_Frame;

    public Wizard(int x, int y, int width, int height, float spellingSpeed){
        this.x = x;
        this.y = y;
        this.spellingSpeed = spellingSpeed;
        this.width = width;
        this.height = height;

        /*Cargar la img*/
        image = new Texture(Gdx.files.internal("Wizard/wizard.png"));
        TextureRegion [][] tmp = TextureRegion.split(image,image.getWidth()/3, image.getHeight()/4);

        /*Wizard Spelling*/
        spellingMovement(tmp);
        /*Wizard Idle*/
        idleMovement(tmp);

        spellingTime = 0f;
        time = 0f;
    }
    public void render(final SpriteBatch batch) {
        for(int i = 0; i < GameHandler.key.length; i++) {
            if(Gdx.input.isKeyPressed(GameHandler.key[i]))
                GameHandler.wizardSpell = true;
        }
        if(GameHandler.wizardSpell) {
            spellingTime += Gdx.graphics.getDeltaTime();
            currentSpelling_Frame = (TextureRegion) spellingAnimation.getKeyFrame(spellingTime, true);
            batch.draw(currentSpelling_Frame, x, y, width, height);
            if (spellingTime >= spellingSpeed*4) {
                GameHandler.wizardSpell = false;
                spellingTime = 0;
            }
        }
        else {
            time += Gdx.graphics.getDeltaTime();
            currentIdle_Frame = (TextureRegion) idleAnimation.getKeyFrame(time, true);
            batch.draw(currentIdle_Frame, x, y, width, height);
        }
    }
    private void spellingMovement(TextureRegion [][] temporal){
        spellingMovement = new TextureRegion[4];
        int w = 0;
        for(int i = 0 ; i<3 ; i++){
            spellingMovement[w] = temporal[2][i];
            w++;
            if(i == 2){
                spellingMovement[w] = temporal[3][0];
                w++;
            }
        }
        spellingAnimation=new Animation<>(spellingSpeed, spellingMovement);
    }
    private void idleMovement(TextureRegion [][] temporal){
        idleMovement = new TextureRegion[6];
        int w = 0;
        for(int i = 0; i<2 ; i++){
            for(int j = 0; j<3 ; j++){
                idleMovement[w] = temporal[i][j];
                w++;
            }
        }
        idleAnimation = new Animation<>(1/9f, idleMovement);
    }
}
