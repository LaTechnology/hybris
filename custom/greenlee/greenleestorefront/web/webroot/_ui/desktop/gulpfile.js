var gulp = require('gulp'),
    sass = require('gulp-ruby-sass')
notify = require("gulp-notify")
bower = require('gulp-bower')
autoprefixer = require('gulp-autoprefixer')
sourcemaps = require('gulp-sourcemaps');
var webpack = require('webpack'),
    Clean = require('clean-webpack-plugin');
var webpack = require('webpack');
var scsslint = require('gulp-scss-lint');
var iconfont = require('gulp-iconfont');
var iconfontCss = require('gulp-iconfont-css');
var csso = require('gulp-csso');
var jscs = require('gulp-jscs');
var jsBeautify = require('gulp-js-prettify');
var sassBeautify = require('gulp-sassbeautify');

var runTimestamp = Math.round(Date.now() / 1000);
var config = {
    sassPath: './resources/sass',
    bowerDir: './bower_components',
    jsPath: './resources/js',
    svgPath: './resources/svg',
}

gulp.task('bower', function() {
    return bower()
        .pipe(gulp.dest(config.bowerDir))
});

gulp.task('icons', function() {
    return gulp.src(config.bowerDir + '/fontawesome/fonts/**.*')
        .pipe(gulp.dest('./theme-greenlee/fonts'));
});

gulp.task('Iconfont', function() {
    return gulp.src([config.svgPath + '/*.svg'])
        .pipe(iconfontCss({
            fontName: 'greenlee-icon',
            path: './resources/templates/_icons.css',
            targetPath: '/_icons.css',
            fontPath: './resources/sass'
        }))
        .pipe(iconfont({
            fontName: 'greenlee-icon', // required
            normalize: true,
            formats: ['ttf', 'eot', 'woff', 'woff2', 'svg'] // default, 'woff2' and 'svg' are available
        }))
        .pipe(gulp.dest('./theme-greenlee/fonts'));
});


// Webpack packaging
var webpackConfig = require('./webpack.config');
gulp.task('scripts', function() {
    webpack(webpackConfig, function(err, stats) {
        if (err) {
            handleErrors();
        }
    });
});
gulp.task('beautify-js', function() {
    gulp.src(['./resources/js/checkout.js'])
        .pipe(jsBeautify({
            collapseWhitespace: true
        }))
        .pipe(jscs({
            fix: true
        }))
        .pipe(gulp.dest('./resources/js/'));
});

gulp.task('scss-lint', function() {
    return gulp.src(config.sassPath + '/**/_about.scss')
        .pipe(scsslint());
});

gulp.task('css', function() {
    return sass([config.sassPath + '/style.scss', config.sassPath + '/payment.scss', config.sassPath + '/print.scss'], {
            style: 'compressed',
            'sourcemap=none': true,
            loadPath: [
                './resources/sass',
                config.bowerDir + '/bootstrap-sass-official/assets/stylesheets',
                config.bowerDir + '/fontawesome/scss',
            ],

        })
        .on("error", notify.onError(function(error) {
            return "Error: " + error.message;
        }))


    .pipe(autoprefixer({
            browsers: ['Android >= 2.3',
                'BlackBerry >= 7',
                'Chrome >= 9',
                'Firefox >= 4',
                'Explorer >= 8',
                'iOS >= 5',
                'Opera >= 11',
                'Safari >= 5',
                'OperaMobile >= 11',
                'OperaMini >= 6',
                'ChromeAndroid >= 9',
                'FirefoxAndroid >= 4',
                'ExplorerMobile >= 9'
            ],
            cascade: false
        }))
        .pipe(csso())
        .pipe(gulp.dest('./theme-greenlee/css'))

});

// Rerun the task when a file changes
gulp.task('watch', function() {
    gulp.watch(config.jsPath + '/**/*.js', ['scripts']);
    gulp.watch(config.sassPath + '/**/*.scss', ['css']);
    gulp.watch(config.svgPath + '/**/*.svg', ['Iconfont']);
});

var cleanCSS = require('gulp-clean-css');

gulp.task('minify-css', function() {
    return gulp.src('./theme-greenlee/css/*.css')
        .pipe(cleanCSS({
            debug: true
        }, function(details) {
            console.log(details.name + ': ' + details.stats.originalSize);
            console.log(details.name + ': ' + details.stats.minifiedSize);
        }))
        .pipe(gulp.dest('./theme-greenlee/css'))
});

gulp.task('beautify-sass', function() {
    return gulp.src('./resources/sass/*.scss')
        .pipe(sassBeautify())
        .pipe(gulp.dest('resources/scss/'));
});

gulp.task('default', ['bower', 'icons', 'css', 'scripts', 'Iconfont']);

gulp.task('dev', ['css', 'scripts', 'minify-css']);

gulp.task('lint', ['scss-lint']);
