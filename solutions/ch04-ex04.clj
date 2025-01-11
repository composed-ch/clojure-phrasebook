(defn next-editor-state [state action]
  (case state
    :clean-unsaved
    (case action
      :edit :dirty-unsaved
      :save :clean-saved)
    :dirty-unsaved
    (case action
      :edit :dirty-unsaved
      :save :clean-saved)
    :clean-saved
    (case action
      :edit :dirty-saved
      :save :clean-saved)
    :dirty-saved
    (case action
      :edit :dirty-saved
      :save :clean-saved)))

