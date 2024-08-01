-- cooper; web game that is a homage to toppler/perestroika.
-- Copyright (C) 2024 Gaelan D'costa (gdcosta@gmail.com)
--
-- This program is free software: you can redistribute it and/or modify it under
-- the terms of the GNU Affero General Public License as published by the Free
-- Software Foundation, either version 3 of the License, or (at your option) any
-- later version.
--
-- This program is distributed in the hope that it will be useful, but WITHOUT
-- ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
-- FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
-- details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with this program. If not, see <https://www.gnu.org/licenses/>.


module Main exposing (main)

import Browser
import Browser.Events
import Html exposing (Html, div, span, text)
import Json.Decode as Decode
import Process
import Task

-- This currently means we have two different screens
-- the screen where the game is played, and a banner screen
-- when you change levels.
type GamePhase
    = Playing
    | NewLevel
    | GameWon

-- Used for positions on the board
type alias Coords =
    -- First :: current row
    -- Second :: current column
    ( Int, Int )

-- A global constant indicating the position where the player should
-- start on a new game board.
startPos : Coords
startPos = (1,1)

-- Current player location row
posRow : Model -> Int
posRow state = Tuple.first state.pos

-- Current player location column
posCol : Model -> Int
posCol state = Tuple.second state.pos

-- Current board size in rows
dimRows : Model -> Int
dimRows state = Tuple.first state.dims

-- Current board size in colums
dimCols : Model -> Int
dimCols state = Tuple.second state.dims

-- Determine board size based on input level
-- Cal to a certain size to not get ridiculous.
genDims : Int -> Coords
genDims lvl =
    let
        cols = min 16 <| 6.0 + toFloat lvl
        rows = cols / 2.0 |> ceiling
    in
        (rows, round cols)

-- Generate petals based on game board size
genCircles : Coords -> List (List Int)
genCircles (rows, cols) = List.map
                          (\_ -> List.map (\_ -> 100) (List.range 1 <| cols))
                          (List.range 1 <| rows)

-- This is the game state
type alias Model =
    -- row/cols: defines the dimension of the game board
    { dims : Coords
    -- current player position
    , pos : Coords

    -- It'd be nice if I could derive dimensions from the content
    -- not additional metadata. For now, this is the current game board state
    , circles : List (List Int)
    , level : Int

    -- This determines what the root view should look like
    , phase : GamePhase
    }

-- List of input messages from the game, either by the user or internally
type Msg
    = Up
    | Down
    | Left
    | Right
    | Invalid
    | ShowBoard

-- Basic Elm framework
--
-- init: is a function that sets up the logic state
-- update: takes the current state and a command from our subscriptions
-- view: takes a state and turns it into HTML that optionally derives from a
-- message.
-- subscriptions: A channel of stuff that comes from the outside world.
main =
    Browser.element
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        }

-- Generate the starting game state.
init : () -> ( Model, Cmd Msg )
init _ =
    let
        startLvl = 1
        dims = genDims startLvl
    in
    ( { -- Player coordinates
        pos = startPos

      -- Board dimensions
      , dims = dims

      -- Game progression
      , level = startLvl

      -- The game starts on the Game Board
      , phase = Playing

      -- For now, generate fully extended petals to start.
      , circles = genCircles dims
      }
    , Cmd.none
    )

-- Get relevant data out of a keyPress event, via javascript parsing.
keyPressDecoder : Decode.Decoder Msg
keyPressDecoder =
    Decode.map handleKeypress (Decode.field "key" Decode.string)

-- Turn relevant keypress data into a game message.
-- We choose to handle the following keyboard scemes:
-- Up/Down/Left/Right
-- vim bindings, hjkl
-- first-person-shooter bindings, wasd
handleKeypress : String -> Msg
handleKeypress input =
    case input of
        "ArrowUp" ->
            Up

        "k" ->
            Up

        "w" ->
            Up

        "ArrowDown" ->
            Down

        "j" ->
            Down

        "s" ->
            Down

        "ArrowLeft" ->
            Left

        "h" ->
            Left

        "a" ->
            Left

        "ArrowRight" ->
            Right

        "l" ->
            Right

        "d" ->
            Right
        -- Ignore invalid inputs.
        _ ->
            Invalid

-- Register to browser keydown events and pass to our encoder
subscriptions : Model -> Sub Msg
subscriptions _ =
    Browser.Events.onKeyDown keyPressDecoder

-- Move the game player across the board without letting it fall off the
-- screen.
-- When you're not on the gameplay screen, don't allow the player to be moved.
movePos : Coords -> Model -> Model
movePos offset state =
    case state.phase of
        Playing ->
            let
                minRow =
                    1

                maxRow =
                    dimRows state

                minCol =
                    1

                maxCol =
                    dimCols state

                offsetRow =
                    Tuple.first offset

                offsetCol =
                    Tuple.second offset

                newRow =
                    posRow state + offsetRow

                newCol =
                    posCol state + offsetCol

                clipRow =
                    max minRow <| min maxRow newRow

                clipCol =
                    max minCol <| min maxCol newCol
            in
            { state | pos = ( clipRow, clipCol ) }

        _ ->
            state

moveUp : Model -> Model
moveUp =
    movePos ( 1, 0 )

moveDown : Model -> Model
moveDown =
    movePos ( -1, 0 )

moveLeft : Model -> Model
moveLeft =
    movePos ( 0, -1 )

moveRight : Model -> Model
moveRight =
    movePos ( 0, 1 )

-- General game progression handler
update : Msg -> Model -> ( Model, Cmd Msg )
update msg state =
    let
        -- Update the player position based on key positions
        mvstate =
            case msg of
                Up ->
                    moveUp state

                Down ->
                    moveDown state

                Left ->
                    moveLeft state

                Right ->
                    moveRight state

                Invalid ->
                    state

                ShowBoard ->
                    { state | phase = Playing }

        -- If the player has reached the goal level, move to next level.
        -- Set the player back to the starting position.
        -- Generate a new game board appropriate to the new level.
        -- Also set the game board to a transition banner (we'll escape from it
        -- later.)
        lvlState =
            if
                mvstate.pos == mvstate.dims
            then
                -- If we were on level 25, we've won the game.
                if mvstate.level == 25
                then
                    { mvstate | phase = GameWon }
                else
                    let newLevel = mvstate.level + 1
                        newDims = genDims newLevel
                    in
                        { mvstate
                            | level = newLevel
                            , dims = newDims
                            , pos = startPos
                            , circles = genCircles newDims
                            , phase = NewLevel
                        }
            else
                mvstate

        -- If we have triggered a new level (see lvlState.phase) send a command
        -- along with the new state that starts a timer for some amount of time
        -- that, when it fires, will set us back to the game board via an
        -- emitted Msg.
        lvlCommand =
            case lvlState.phase of
                NewLevel ->
                    Process.sleep 2000 |> Task.perform (always ShowBoard)

                _ ->
                    Cmd.none
    in
    ( lvlState
    , lvlCommand
    )


newLevelView : Model -> Html Msg
newLevelView state =
    div []
        [ text "NEW LEVEL YOOOOOOO !!!! ENTERING LEVEL "
        , text (String.fromInt state.level)
        ]

gameWonView : Model -> Html Msg
gameWonView state =
    div []
        [ text "YOU HAVE WON THE GAME!!! CONGRATULATIONS!!!!!"
        ]


gameView : Model -> Html Msg
gameView state =
    -- Board div
    div []
        -- Render each row. This probably should be its own function for
        -- readability.
        (List.indexedMap
            (\indexr row ->
                div []
                    -- Render each column
                    (List.indexedMap
                        (\indexc col ->
                            span []
                                -- Pad each value by a space on each side
                                [ text " "
                                , text
                                    (String.fromInt
                                        col
                                    )
                                , if
                                    -- Print "*" if the player is located here
                                    -- an empty space otherwise.
                                    (indexr + 1)
                                        -- I want player to start at bottom left
                                        -- and move up-rightwards.
                                        -- But nature of board will be to start
                                        -- at top left and move down-rightwards.
                                        == (dimRows state)
                                        - (posRow state)
                                        + 1
                                        && indexc
                                        + 1
                                        == (posCol state)
                                  then
                                    text "*"

                                  else
                                    text " "
                                ]
                        )
                        row
                    )
            )
            state.circles
            -- Print the player position coordinates for debugging purposes.
            ++ [ div []
                    [ text "Position: "
                    , text (String.fromInt (Tuple.first state.pos))
                    , text " "
                    , text (String.fromInt (Tuple.second state.pos))
                    ]
               -- Print the current level for debugging purposes
               , div []
                    [ text "Level: "
                    , text (String.fromInt state.level)
                    , text " Rows: "
                    , text (String.fromInt <| dimRows state)
                    , text " Columns: "
                    , text (String.fromInt <| dimCols state)
                    ]
               -- Print an App title, for silly reasons
               , div []
                    [ text "HACKDAY TOPPLER 0.0000000000000000001"
                    ]
               ]
        )

-- Based on the game phase, pick the view to render.
view : Model -> Html Msg
view state =
    let
        curView =
            case state.phase of
                NewLevel ->
                    newLevelView

                GameWon ->
                    gameWonView

                Playing ->
                    gameView
    in
    curView state
