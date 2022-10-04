
import * as THREE from 'three';
import { ArcballControls } from 'three/addons/controls/ArcballControls.js';
import { OBJLoader } from 'three/addons/loaders/OBJLoader.js';
const perspectiveDistance = 2.5;
let camera, controls, scene, renderer;
init();
function init() {
    const container = document.createElement('div');
    document.body.appendChild(container);
    renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.outputEncoding = THREE.sRGBEncoding;
    renderer.toneMapping = THREE.ReinhardToneMapping;
    renderer.toneMappingExposure = 3;
    renderer.domElement.style.background = 'linear-gradient( 180deg, rgba( 0,0,0,1 ) 0%, rgba( 128,128,255,1 ) 100% )';
    container.appendChild(renderer.domElement);

    scene = new THREE.Scene();
    scene.add(new THREE.AmbientLight(0xffffff, 0.6));
    const axesHelper = new THREE.AxesHelper(290);
    scene.add(axesHelper);
    camera = makePerspectiveCamera();
    camera.position.set(290, 290, 290);
    const material = new THREE.MeshLambertMaterial({ color: 0xfb8e00 });
    new OBJLoader()
        .setPath('models/obj/cerberus/')
        .load('punto_di_presa_robot.obj', function (group) {
            // const textureLoader = new THREE.TextureLoader().setPath('models/obj/cerberus/');
            // material.roughness = 1;
            // material.metalness = 1;
            // const diffuseMap = textureLoader.load('Cerberus_A.jpg', render);
            // diffuseMap.encoding = THREE.sRGBEncoding;
            // material.map = diffuseMap;
            // material.metalnessMap = material.roughnessMap = textureLoader.load('Cerberus_RM.jpg', render);
            // material.normalMap = textureLoader.load('Cerberus_N.jpg', render);
            // material.map.wrapS = THREE.RepeatWrapping;
            // material.roughnessMap.wrapS = THREE.RepeatWrapping;
            // material.metalnessMap.wrapS = THREE.RepeatWrapping;
            // material.normalMap.wrapS = THREE.RepeatWrapping;
            group.traverse(function (child) {
                if (child instanceof THREE.Mesh) {
                    var geometry = new THREE.EdgesGeometry(child.geometry);
                    var material = new THREE.LineBasicMaterial({ color: 0xffffff });
                    var wireframe = new THREE.LineSegments(geometry, material);
                    scene.add(wireframe);
                }
            });
            // group.rotation.y = Math.PI / 2;
            // group.position.x += 0.25;
            scene.add(group);
            render();
            window.addEventListener('resize', onWindowResize);
            setArcballControls();
            render();
        });
}

function setArcballControls() {
    controls = new ArcballControls(camera, renderer.domElement, scene);
    controls.addEventListener('change', render);
    controls.setGizmosVisible(false);
}

function makePerspectiveCamera() {
    const fov = 75;
    const aspect = window.innerWidth / window.innerHeight;
    const near = 1;
    const far = 2000;
    const newCamera = new THREE.PerspectiveCamera(fov, aspect, near, far);
    return newCamera;
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
    render();
}

function render() {
    renderer.render(scene, camera);
}