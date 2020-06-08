with import <nixpkgs> {};

mkShell {
  buildInputs = [ clojure clj-kondo ];
}
