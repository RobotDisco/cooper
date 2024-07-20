module Main exposing (main)

import Browser
import Html exposing (Html, div, span, text)


type alias Model =
    { rows : Int
    , cols : Int
    , prow : Int
    , pcol : Int

    -- It'd be nice if I could derive dimensions from the content
    -- not additional metadata.
    , circles : List (List Int)
    }


main =
    Browser.sandbox { init = init, update = update, view = view }


init : Model
init =
    let
        rows =
            4

        cols =
            7
    in
    { prow = 0
    , pcol = 0
    , rows = rows
    , cols = cols

    -- For now, generate fully extended petals to start.
    , circles =
        List.map
            (\_ -> List.map (\_ -> 100) (List.range 1 cols))
            (List.range 1 rows)
    }


update : Model -> Model -> Model
update state _ =
    state


view : Model -> Html Model
view model =
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
                                    indexr
                                        -- I want player to start at bottom left
                                        -- and move up-rightwards.
                                        -- But nature of board will be to start
                                        -- at top left and move down-rightwards.
                                        == ((model.rows - 1) - model.prow)
                                        && indexc
                                        == model.pcol
                                  then
                                    text "*"

                                  else
                                    text " "
                                ]
                        )
                        row
                    )
            )
            model.circles
        )
