from django.contrib.auth.models import User
from rest_framework import serializers

from smart_running.models import Route, Marker, UserProfile


class RouteSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Route
        fields = ('url', 'publisher', 'date_published', 'last_updated', 'title', 'description',
                  'mode', 'difficulty', 'markers')  # markers too
        read_only_fields = ('publisher', 'date_published', 'last_updated')
        # TODO set read only fields on create()


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'first_name', 'last_name', 'email', 'password', 'profile')
        # TODO password write only


class UserProfileSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = UserProfile
        read_only_fields = ('email_verified',)


class MarkerSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Marker
        fields = ('url', 'title', 'description', 'full_description', 'latitude',
                  'longitude', 'zone_radius')
