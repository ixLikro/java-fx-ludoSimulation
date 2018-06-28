package test;

import mainWindow.Controller;
import mainWindow.model.Board;
import mainWindow.model.Color;
import mainWindow.model.Game;
import mainWindow.model.Player;
import mainWindow.model.field.Field;
import mainWindow.model.field.FinishField;
import mainWindow.model.field.StartField;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;


class BoardTest {

    private Game game;
    private Board board;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        game = new Game();
        board = game.getBoard();
    }

    private enum FieldType{
        normal, finish, start
    }

    private Field testFieldType(Field oldField, Color color, int diceRoll, FieldType expectedFieldType){
        Field newField = board.calculateNewField(oldField, color, diceRoll);

        switch (expectedFieldType) {
            case normal:
                assertTrue(!(newField instanceof FinishField) && !(newField instanceof StartField), "calculateNewField should return a normal field");
                break;
            case finish:
                assertTrue(newField instanceof FinishField, "calculateNewField should return a finish field");
                break;
            case start:
                assertTrue(newField instanceof StartField, "calculateNewField should return a start field");
                break;
        }

        return newField;
    }

    @Test
    void calculateNewField() {

        //test normal fields
        assertEquals(5, testFieldType(board.getAllNormalFields().get(0),Color.GREEN,5, FieldType.normal).getIndex());
        assertEquals(2, testFieldType(board.getAllNormalFields().get(38), Color.RED, 4, FieldType.normal).getIndex());

        //test cant move further
        game.getAllPlayer().forEach(player -> assertEquals(board.getLastNormalField(player.getColor()).getIndex()
                , testFieldType(board.getLastNormalField(player.getColor()), player.getColor(), 6, FieldType.normal).getIndex()
                , "calculateNewField should return the same field"));

        //test go out
        game.getAllPlayer().forEach(player -> {
            switch (player.getColor()) {
                case GREEN:
                    assertEquals(0, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 2, FieldType.start).getIndex());
                    assertEquals(0, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 6, FieldType.normal).getIndex());
                    break;
                case RED:
                    assertEquals(0, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 2, FieldType.start).getIndex());
                    assertEquals(10, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 6, FieldType.normal).getIndex());
                    break;
                case BLUE:
                    assertEquals(0, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 2, FieldType.start).getIndex());
                    assertEquals(20, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 6, FieldType.normal).getIndex());
                    break;
                case YELLOW:
                    assertEquals(0, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 2, FieldType.start).getIndex());
                    assertEquals(30, testFieldType(board.getStartFields(player.getColor()).get(0), player.getColor(), 6, FieldType.normal).getIndex());
                    break;
            }
        });

        //test go in
        game.getAllPlayer().forEach(player -> {
            assertEquals(0, testFieldType(board.getAllNormalFields().get(board.getLastNormalField(player.getColor())
                    .getIndex()-1), player.getColor(), 2, FieldType.finish).getIndex());
            assertEquals(3, testFieldType(board.getAllNormalFields().get(board.getLastNormalField(player.getColor())
                    .getIndex()-1), player.getColor(), 5, FieldType.finish).getIndex());
        });

        //test move up in the finish
        game.getAllPlayer().forEach(player -> {
            assertEquals(3, testFieldType(board.getFinishFields(player.getColor()).get(3), player.getColor(),1, FieldType.finish).getIndex());
            assertEquals(3, testFieldType(board.getFinishFields(player.getColor()).get(1), player.getColor(),2, FieldType.finish).getIndex());
            assertEquals(0, testFieldType(board.getFinishFields(player.getColor()).get(0), player.getColor(),4, FieldType.finish).getIndex());
        });
    }

    @Test
    void calculateDiffToLastField() {
        assertEquals(board.calculateDiffToLastField(new StartField(2, Color.GREEN), Color.GREEN),-1, "calculateDiffToLastField: should return -1, bc a startField has no diff to a last normal field");
        assertEquals(board.calculateDiffToLastField(new FinishField(0, Color.RED), Color.YELLOW),-1, "calculateDiffToLastField: should return -1, bc a finishField has no diff to a last normal field");
        //green
        assertEquals(39, board.calculateDiffToLastField(board.getAllNormalFields().get(0), Color.GREEN));
        assertEquals(0 , board.calculateDiffToLastField(board.getAllNormalFields().get(39), Color.GREEN));
        assertEquals(1 , board.calculateDiffToLastField(board.getAllNormalFields().get(38), Color.GREEN));
        assertEquals(24, board.calculateDiffToLastField(board.getAllNormalFields().get(15), Color.GREEN));

        //red
        assertEquals(39, board.calculateDiffToLastField(board.getAllNormalFields().get(10),  Color.RED));
        assertEquals(0 , board.calculateDiffToLastField(board.getAllNormalFields().get(9), Color.RED));
        assertEquals(1 , board.calculateDiffToLastField(board.getAllNormalFields().get(8), Color.RED));
        assertEquals(10, board.calculateDiffToLastField(board.getAllNormalFields().get(39), Color.RED));
        assertEquals(21, board.calculateDiffToLastField(board.getAllNormalFields().get(28), Color.RED));

        //blue
        assertEquals(39, board.calculateDiffToLastField(board.getAllNormalFields().get(20), Color.BLUE));
        assertEquals(0 , board.calculateDiffToLastField(board.getAllNormalFields().get(19),  Color.BLUE));
        assertEquals(1 , board.calculateDiffToLastField(board.getAllNormalFields().get(18),  Color.BLUE));
        assertEquals(19, board.calculateDiffToLastField(board.getAllNormalFields().get(0), Color.BLUE));
        assertEquals(31, board.calculateDiffToLastField(board.getAllNormalFields().get(28), Color.BLUE));

        //yellow
        assertEquals(39, board.calculateDiffToLastField(board.getAllNormalFields().get(30),  Color.YELLOW));
        assertEquals(0 , board.calculateDiffToLastField(board.getAllNormalFields().get(29),  Color.YELLOW));
        assertEquals(1 , board.calculateDiffToLastField(board.getAllNormalFields().get(28),  Color.YELLOW));
        assertEquals(29, board.calculateDiffToLastField(board.getAllNormalFields().get(0),   Color.YELLOW));
        assertEquals(19, board.calculateDiffToLastField(board.getAllNormalFields().get(10),  Color.YELLOW));
    }

    @Test
    void getFreeStartField() {
        game.getAllPlayer().forEach(player -> assertNull(board.getFreeStartField(player)
                , "getFreeStartField have to return NULL! Since no startField are available"));

        game.getAllPlayer().forEach(player -> {
            Consumer<Player> testStartField = currentPlayer -> {
                StartField startField = null;

                try {
                    startField = (StartField) board.getFreeStartField(player);
                } catch (ClassCastException e) {
                    fail("getFreeStartField have to retrun a StartField!");
                }

                assertNotNull(startField, "getFreeStartField have to return something!");
                assertEquals(startField.getColor(), player.getColor(), "getFreeStartField  returned the wrong color");
                final StartField finalStartField = startField;
                player.getAllFigures().forEach(figure -> assertNotSame(figure.getIsOn(), finalStartField, "getFreeStartField returned a wron field!"));
            };

            //remove one
            player.getAllFigures().get(0).setIsOn(new Field(0));
            testStartField.accept(player);

            //remove all
            player.getAllFigures().forEach(figure -> figure.setIsOn(new Field(0)));
            testStartField.accept(player);

        });
    }

    @Test
    void getStartFields() {
        game.getAllPlayer().forEach(player -> {
            assertEquals(board.getStartFields(player.getColor()).size(), Controller.FIGURE_COUNT, "getStartFields have to return 4 fields");
            board.getStartFields(player.getColor()).forEach(startField -> {
                assertNotNull(startField,"getStartFields returned at least one null...");
            });
        });
    }

    @Test
    void getFinnishFields() {
        game.getAllPlayer().forEach(player -> {
            assertEquals(board.getFinishFields(player.getColor()).size(), Controller.FIGURE_COUNT, "getFinishFields have to return 4 fields");
            board.getFinishFields(player.getColor()).forEach(startField -> {
                assertNotNull(startField,"getFinishFields returned at least one null...");
            });
        });
    }

    @Test
    void getNormalSpawnField() {
        game.getAllPlayer().forEach(player -> {
            assertNotNull(board.getNormalSpawnField(player.getColor()), "getNormalSpawnField returned NULL...");
            switch (player.getColor()){
                case GREEN:
                    assertEquals(board.getNormalSpawnField(player.getColor()).getIndex(), 0, "The GREEN startField should be have the index 0");
                    break;
                case RED:
                    assertEquals(board.getNormalSpawnField(player.getColor()).getIndex(), 10, "The RED startField should be have the index 10");
                    break;
                case BLUE:
                    assertEquals(board.getNormalSpawnField(player.getColor()).getIndex(), 20, "The BLUE startField should be have the index 20");
                    break;
                case YELLOW:
                    assertEquals(board.getNormalSpawnField(player.getColor()).getIndex(), 30, "The YELLOW startField should be have the index 30");
                    break;
            }
        });
    }

    @Test
    void getLastNormalField() {
        game.getAllPlayer().forEach(player -> {
            assertNotNull(board.getLastNormalField(player.getColor()), "getLastNormalField returned NULL...");
            switch (player.getColor()){
                case GREEN:
                    assertEquals(board.getLastNormalField(player.getColor()).getIndex(), 39, "The GREEN lastNormalField should be have the index 39");
                    break;
                case RED:
                    assertEquals(board.getLastNormalField(player.getColor()).getIndex(), 9, "The RED lastNormalField should be have the index 9");
                    break;
                case BLUE:
                    assertEquals(board.getLastNormalField(player.getColor()).getIndex(), 19, "The BLUE lastNormalField should be have the index 19");
                    break;
                case YELLOW:
                    assertEquals(board.getLastNormalField(player.getColor()).getIndex(), 29, "The YELLOW lastNormalField should be have the index 29");
                    break;
            }
        });
    }
}