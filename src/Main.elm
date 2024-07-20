module Main exposing (main)

import Browser
import Html exposing (Html, text)

main = Browser.sandbox { init = 0, update = update, view = view }
                             
update : Int -> Int -> Int
update _ _ = 0

view : Int -> Html msg
view _ = text "Hello"
