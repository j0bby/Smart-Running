from rest_framework import serializers

from smart_running.models import Route, CustomUser, Marker


class RouteSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Route
        fields = ('url', 'publisher', 'date_published', 'last_updated', 'title', 'description',
                  'mode', 'difficulty', 'markers')  # markers too
        read_only_fields = ('publisher', 'date_published', 'last_updated')
        # TODO set read only fields on create()


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = CustomUser
        fields = ('url', 'first_name', 'last_name', 'email', 'password', 'profile',
                  'birth_date', 'email_verified')
        read_only_fields = ('email_verified',)
        # TODO password write only


class MarkerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Marker
        fields = ('url', 'title', 'description', 'full_description', 'latitude',
                  'longitude', 'zone_radius')
