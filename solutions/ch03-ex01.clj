(def server {:name "Persephone" :uptime "13d7h5m43s" :active false})
(assoc (assoc (dissoc server :uptime) :active false) :decommissioned 2025)

