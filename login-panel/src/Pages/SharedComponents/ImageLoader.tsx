const importAll = (r: __WebpackModuleApi.RequireContext) => {
    const images: { [key: string]: string } = {};
    r.keys().forEach((item: string) => {
        images[item.replace('./', '')] = r(item).default;
    });
    return images;
};

const images = require.context('Images', false, /\.(png|jpe?g|svg)$/);

export default importAll(images);


