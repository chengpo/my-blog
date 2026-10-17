define(['loading-progress/loading-progress.module'],
   function(loadingProgressModule) {
   'use strict'; 
    
   loadingProgressModule.component('loadingProgress', {
    bindings: {
        inProgress: '<'
    },
    controller: ['$interval', function LoadingProgressController($interval) {
        var self = this;
        var dotInterval = null;
        var dotCount = 0;
        var barIndex = 0;

        var barList = ['-', '\\', '|', '/'];

        function updateDots() {
            barIndex = (barIndex + 1) % barList.length;
            var bar = '[' + barList[barIndex] + ']';
            dotCount = (dotCount + 1) % 10;
            var dots = '.'.repeat(dotCount);
            self.progress = bar + ' Loading ' + dots
        }

        self.$onChanges = function (changesObj) {
            var inProgress = false;
            if (changesObj.inProgress) {
                inProgress = changesObj.inProgress.currentValue;
            }
            if (inProgress) {
                if (dotInterval == null) {
                    dotInterval = $interval(updateDots, 350);
                }
            } else {
                if (dotInterval != null) { 
                    $interval.cancel(dotInterval);
                    dotInterval = null;
                    self.progress = null;
                }
            }
        }
    }],
    template: `
    <div class="container" style="position: relative; min-height: 500px;" ng_if="$ctrl.progress" > 
        <div class="row">
            <div class="alert alert-info centered" >
                <strong>{{$ctrl.progress}}</strong>
            </div>
        </div>
    </div>
    `
   });
});