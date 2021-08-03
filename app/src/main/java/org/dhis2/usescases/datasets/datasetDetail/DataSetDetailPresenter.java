package org.dhis2.usescases.datasets.datasetDetail;

import androidx.annotation.VisibleForTesting;

import org.dhis2.data.filter.FilterRepository;
import org.dhis2.data.schedulers.SchedulerProvider;
import org.dhis2.utils.analytics.matomo.MatomoAnalyticsController;
import org.dhis2.utils.filters.DisableHomeFiltersFromSettingsApp;
import org.dhis2.utils.filters.FilterItem;
import org.dhis2.utils.filters.FilterManager;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static org.dhis2.utils.analytics.matomo.Actions.SYNC_DATASET;
import static org.dhis2.utils.analytics.matomo.Categories.DATASET_LIST;
import static org.dhis2.utils.analytics.matomo.Labels.CLICK;

public class DataSetDetailPresenter {

    private DataSetDetailView view;
    private DataSetDetailRepository dataSetDetailRepository;
    private SchedulerProvider schedulerProvider;
    private FilterManager filterManager;
    private FilterRepository filterRepository;
    private DisableHomeFiltersFromSettingsApp disableHomFilters;

    CompositeDisposable disposable;

    public DataSetDetailPresenter(DataSetDetailView view,
                                  DataSetDetailRepository dataSetDetailRepository,
                                  SchedulerProvider schedulerProvider,
                                  FilterManager filterManager,
                                  FilterRepository filterRepository,
                                  DisableHomeFiltersFromSettingsApp disableHomFilters) {

        this.view = view;
        this.dataSetDetailRepository = dataSetDetailRepository;
        this.schedulerProvider = schedulerProvider;
        this.filterManager = filterManager;
        this.filterRepository = filterRepository;
        this.disableHomFilters = disableHomFilters;
        disposable = new CompositeDisposable();
    }

    public void init() {
        getOrgUnits();

        disposable.add(
                filterManager.asFlowable().startWith(filterManager)
                        .flatMap(filterManager -> Flowable.just(filterRepository.dataSetFilters(dataSetDetailRepository.getDataSetUid())))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(filterItems -> {
                                    if (filterItems.isEmpty()){
                                        view.hideFilters();
                                    } else {
                                        view.setFilters(filterItems);
                                    }
                                    view.updateFilters(filterManager.getTotalFilters());
                                },
                                Timber::d
                        )
        );

        disposable.add(
                filterManager.getPeriodRequest()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(
                                periodRequest -> view.showPeriodRequest(periodRequest.getFirst()),
                                Timber::e
                        ));



        disposable.add(FilterManager.getInstance().getCatComboRequest()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        catComboUid -> view.showCatOptComboDialog(catComboUid),
                        Timber::e
                )
        );
    }

    public void onBackClick() {
        view.back();
    }

    public void showFilter() {
        view.showHideFilter();
    }

    @VisibleForTesting
    public void getOrgUnits() {
        disposable.add(
                filterManager.ouTreeFlowable()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(
                                open -> view.openOrgUnitTreeSelector(),
                                Timber::e
                        )
        );
    }


    public void onDettach() {
        disposable.clear();
    }

    public void displayMessage(String message) {
        view.displayMessage(message);
    }

    public void clearFilterClick() {
        filterManager.clearAllFilters();
        view.clearFilters();
    }

    public void filterCatOptCombo(String selectedCatOptionCombo) {
        FilterManager.getInstance().addCatOptCombo(
                dataSetDetailRepository.getCatOptCombo(selectedCatOptionCombo)
        );
    }

    public void clearFilterIfDatasetConfig() {
        List<FilterItem> filters = filterRepository.homeFilters();
        disableHomFilters.execute(filters);
    }

    public void setOpeningFilterToNone(){
        filterRepository.collapseAllFilters();
    }
}
