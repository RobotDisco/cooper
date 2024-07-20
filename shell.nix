{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  nativeBuildInputs = [
    pkgs.elmPackages.elm
    pkgs.elmPackages.elm-format
    pkgs.elmPackages.elm-language-server
  ];
}
