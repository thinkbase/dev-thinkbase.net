/** Webpack configuration for distribution */

/** The resolve path list to find modules and loaders */
var MODULE_PATH = [process.cwd(), process.cwd() + '/src', process.cwd() + '/node_modules'];

module.exports = {
    devtool: "source-map",      //Force create source map file
    entry: './src/index.js',
    output: {
        path: __dirname + "/dist",
        publicPath: "/dist/",    //Importment: MUST refer `bundle.js` with `<script src="/dist/bundle.js"></script>`
        filename: 'bundle.js'
    },
    resolve: {
        root: MODULE_PATH,
        alias: {},
        extensions: ['', '.js', '.css', '.html', '.png', '.jpg']
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.(png|jpg)$/, loader: 'url-loader?limit=256' }, // inline base64 URLs for <=256bytes images, direct URLs for the rest
            { test: /\.html$/, loader: 'html-loader' }
        ]
    },
    resolveLoader: {
        modulesDirectories: MODULE_PATH
    }
}
