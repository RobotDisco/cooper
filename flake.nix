{
  description = "Cooper memorial game";
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages."${system}";
    in
      {
        devShells."${system}".default = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            clojure
            # static analysis for Clojure(script) files
            clojure-lsp
            # static analysis for flake.nix file
            rnix-lsp
            git
          ];
        };
      };
}
