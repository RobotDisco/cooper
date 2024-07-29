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


type alias Model =
    { rows : Int
    , cols : Int
    , prow : Int
    , pcol : Int

    -- It'd be nice if I could derive dimensions from the content
    -- not additional metadata.
    , circles : List (List Int)
    , level : Int
    }


type Msg
    = Up
    | Down
    | Left
    | Right
    | Invalid


main =
    Browser.element
        { init = init
        , update = update
        , view = view
        , subscriptions = subscriptions
        }


init : () -> ( Model, Cmd Msg )
init _ =
    let
        rows =
            4

        cols =
            7
    in
    ( { -- Player coordinates
        prow = 1
      , pcol = 1

      -- Board dimensions
      , rows = rows
      , cols = cols

      -- Game progression
      , level = 1

      -- For now, generate fully extended petals to start.
      , circles =
            List.map
                (\_ -> List.map (\_ -> 100) (List.range 1 cols))
                (List.range 1 rows)
      }
    , Cmd.none
    )


keyPressDecoder : Decode.Decoder Msg
keyPressDecoder =
    Decode.map handleKeypress (Decode.field "key" Decode.string)


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

        _ ->
            Invalid


subscriptions : Model -> Sub Msg
subscriptions _ =
    Browser.Events.onKeyDown keyPressDecoder


update : Msg -> Model -> ( Model, Cmd msg )
update msg state =
    ( case msg of
        Up ->
            { state | prow = min (state.prow + 1) state.rows }

        Down ->
            { state | prow = max (state.prow - 1) 1 }

        Left ->
            { state | pcol = max (state.pcol - 1) 1 }

        Right ->
            { state | pcol = min (state.pcol + 1) state.cols }

        Invalid ->
            state
    , Cmd.none
    )


view : Model -> Html Msg
view state =
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
                                        == state.rows
                                        - state.prow
                                        + 1
                                        && indexc
                                        + 1
                                        == state.pcol
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
                    [ text (String.fromInt state.prow)
                    , text " "
                    , text (String.fromInt state.pcol)
                    ]
               , div []
                    [ text "Level: "
                    , text (String.fromInt state.level)
                    ]
               , div []
                    [ text "HACKDAY TOPPLER 0.0000000000000000001"
                    ]
               ]
        )
