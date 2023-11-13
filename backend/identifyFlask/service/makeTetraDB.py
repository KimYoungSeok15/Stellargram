import tetra3

tetraModule = tetra3.Tetra3(load_database=None)


def make_database():
    tetraModule.generate_database(max_fov=30,save_as="test_database",star_catalog='tyc_main', pattern_stars_per_fov=4, verification_stars_per_fov=10)