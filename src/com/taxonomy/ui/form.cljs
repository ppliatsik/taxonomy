(ns com.taxonomy.ui.form
  (:require [re-frame.core :as rf]
            [com.taxonomy.translations :as trans]))

(def icon-shorthands
  {:question-mark 'fa-question-circle
   :cancel        'fa-times-circle
   :camera        'fa-camera
   :trash         'fa-trash
   :mobile        'fa-mobile
   :phone         'fa-phone
   :envelope      'fa-envelope
   :user          'fa-user
   :check         'fa-check-circle
   :search        'fa-search
   :plus          'fa-plus
   :minus         'fa-minus
   :right         'fa-arrow-right
   :left          'fa-arrow-left})

(defn icons
  [{:keys [icon icon-css] :as options}]
  [:span.icon
   [:i.fas {:class (str icon-css " " (get icon-shorthands icon :question-mark))}]])

(defn pagination
  [pagination language pagination-event]
  [:div.columns
   [:div.column.is-12.is-fullwidth
    [:nav.pagination.is-centered {:role "navigation" :aria-label "pagination"}
     [:a.pagination-previous.has-background-success.has-text-white
      {:on-click (fn []
                   (when (< (:first-page pagination) (:current-page pagination))
                     (rf/dispatch [pagination-event (dec (:current-page pagination))])))}
      (trans/translate language :com.taxonomy.ui/previous-page)]
     [:a.pagination-next.has-background-success.has-text-white
      {:on-click (fn []
                   (when (< (:current-page pagination) (:last-page pagination))
                     (rf/dispatch [pagination-event (inc (:current-page pagination))])))}
      (trans/translate language :com.taxonomy.ui/next-page)]
     [:ul.pagination-list
      [:li (when-not (= (:first-page pagination) (:current-page pagination))
             [:a.pagination-link
              {:on-click (fn []
                           (rf/dispatch [pagination-event (:first-page pagination)]))}
              (str (:first-page pagination))])]
      [:li (when-not (= (:first-page pagination) (:current-page pagination))
             [:span.pagination-ellipsis "..."])]
      [:li [:a.pagination-link.is-current
            {:on-click (fn []
                         (rf/dispatch [pagination-event (:current-page pagination)]))}
            (str (:current-page pagination))]]
      [:li (when-not (<= (:last-page pagination) (:current-page pagination))
             [:span.pagination-ellipsis "..."])]
      [:li (when-not (<= (:last-page pagination) (:current-page pagination))
             [:a.pagination-link
              {:on-click (fn []
                           (rf/dispatch [pagination-event (:last-page pagination)]))}
              (str (:last-page pagination))])]]]]])

(defn delete-confirmation-dialog
  [delete-event hide-event language]
  [:div.modal.is-active
   [:div.modal-background
    {:on-click #(rf/dispatch [hide-event])}]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title
      (trans/translate language :com.taxonomy.ui/delete-dialog-box)]]
    [:section.modal-card-body
     [:p (trans/translate language :com.taxonomy.ui/delete-dialog-box-confirmation-msg)]]
    [:footer.modal-card-foot
     [:div.buttons
      [:button.button.is-success
       {:on-click #(rf/dispatch [delete-event])}
       (trans/translate language :com.taxonomy.ui/yes)]
      [:button.button.is-danger
       {:on-click #(rf/dispatch [hide-event])}
       (trans/translate language :com.taxonomy.ui/no)]]]]])
