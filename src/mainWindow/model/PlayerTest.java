package mainWindow.model;

import mainWindow.Controller;
import mainWindow.model.field.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Game game;


    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void isOnField() {

        Field greenStartField = game.getBoard().getStartFields(Color.GREEN).get(0);
        assertAll("greenStartField"
                , () -> assertNotNull(game.getAllPlayer().get(0).isOnField(greenStartField))
                , () -> assertNull(game.getAllPlayer().get(1).isOnField(greenStartField))
                , () -> assertNull(game.getAllPlayer().get(2).isOnField(greenStartField))
                , () -> assertNull(game.getAllPlayer().get(3).isOnField(greenStartField)));

        Field notARealField = new Field(2);
        assertAll("notARealField"
                , () -> assertNull(game.getAllPlayer().get(0).isOnField(notARealField))
                , () -> assertNull(game.getAllPlayer().get(1).isOnField(notARealField))
                , () -> assertNull(game.getAllPlayer().get(2).isOnField(notARealField))
                , () -> assertNull(game.getAllPlayer().get(3).isOnField(notARealField)));

        Field redFinnishField = game.getBoard().getFinishFields(Color.RED).get(3);
        game.getAllPlayer().get(1).getAllFigures().get(2).setIsOn(game.getBoard().getFinishFields(Color.RED).get(3));
        assertAll("redFinnishField"
                , () -> assertNull(game.getAllPlayer().get(0).isOnField(redFinnishField))
                , () -> assertNotNull(game.getAllPlayer().get(1).isOnField(redFinnishField))
                , () -> assertNull(game.getAllPlayer().get(2).isOnField(redFinnishField))
                , () -> assertNull(game.getAllPlayer().get(3).isOnField(redFinnishField)));

        Field normalField = game.getBoard().getAllNormalFields().get(39);
        game.getAllPlayer().get(3).getAllFigures().get(3).setIsOn( game.getBoard().getAllNormalFields().get(39));
        assertAll("normalField"
                , () -> assertNull(game.getAllPlayer().get(0).isOnField(normalField))
                , () -> assertNull(game.getAllPlayer().get(1).isOnField(normalField))
                , () -> assertNull(game.getAllPlayer().get(2).isOnField(normalField))
                , () -> assertNotNull(game.getAllPlayer().get(3).isOnField(normalField)));
    }

    @Test
    void isCleanedUp() {
        //test all figures at start fields
        game.getAllPlayer().forEach(player -> assertEquals(0, player.isCleanedUp(game.getBoard())));

        game.getAllPlayer().forEach(player ->{
            player.getAllFigures().get(0).setIsOn(game.getBoard().getFinishFields(player.getColor()).get(3));
            player.getAllFigures().get(1).setIsOn(game.getBoard().getFinishFields(player.getColor()).get(0));
            assertEquals(-1, player.isCleanedUp(game.getBoard()), "every player have a 2 field gab in the finish field -> so nothing is cleaned up");
        });

        game.getAllPlayer().forEach(player ->{
            player.getAllFigures().get(1).setIsOn(game.getBoard().getFinishFields(player.getColor()).get(2));
            assertEquals(2, player.isCleanedUp(game.getBoard()), "every player have a 2 figures in the finish fields -> so isCleanedUp should return 2");
        });

        //reset
        setUp();

        game.getAllPlayer().forEach(player ->{
            player.getAllFigures().get(0).setIsOn(game.getBoard().getAllNormalFields().get(15));
            assertEquals(-1, player.isCleanedUp(game.getBoard()), "isCleanedUp shoeld return -1 bc the player have a figure on a normal field");
        });

        //reset
        setUp();
        game.getAllPlayer().forEach(player ->{
            for (int i = 0; i < Controller.FIGURE_COUNT; i++){
                player.getAllFigures().get(i).setIsOn(game.getBoard().getFinishFields(player.getColor()).get(i));
            }
            assertEquals(Controller.FIGURE_COUNT, player.isCleanedUp(game.getBoard()));
        });
    }
}