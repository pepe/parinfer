(ns parinfer.api-js
  "Exports a JavaScript module with function hooks into Parinfer."
  (:require
    [parinfer.indent-mode :as indent-mode]
    [parinfer.paren-mode :as paren-mode]))

;;-----------------------------------------------------------------------------
;; JS Function Wrappers
;;-----------------------------------------------------------------------------

(def opt->cljs
  {"cursorLine" :cursor-line
   "cursorX" :cursor-x
   "lineNo" :line-no
   "newLine" :new-line})

(defn convert-opts
  "Convert the JS options object to a clojure map"
  [js-opts]
  (reduce
    (fn [clj-opts [js-key value]]
      (let [key- (or (opt->cljs js-key) (keyword js-key))]
        (assoc clj-opts key- value)))
    {}
    (js->clj js-opts)))

(defn js-result
  "Convert the result to a JS object"
  [{:keys [text valid? state]}]
  #js {:text text
       :isValid valid?
       :state state})

(defn ^:export js-indent-mode
  "JavaScript wrapper around parinfer.indent-mode/format-text"
  [txt js-opts]
  (js-result
    (indent-mode/format-text
      txt
      (convert-opts js-opts))))

(defn ^:export js-indent-mode-change
  "JavaScript wrapper around parinfer.indent-mode/format-text-change"
  [txt prev-state js-change js-opts]
  (js-result
    (indent-mode/format-text-change
      prev-state
      (convert-opts js-change)
      (convert-opts js-opts))))

(defn ^:export js-paren-mode
  "JavaScript wrapper around parinfer.paren-mode/format-text"
  [txt js-opts]
  (js-result
    (paren-mode/format-text
      txt
      (convert-opts js-opts))))
